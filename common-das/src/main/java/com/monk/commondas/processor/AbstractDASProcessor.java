package com.monk.commondas.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.monk.commondas.OutputParameters;
import com.monk.commondas.bean.DataObject;
import com.monk.commondas.bean.Field;
import com.monk.commondas.bean.Key;
import com.monk.commondas.bean.Table;
import com.monk.commondas.cache.DataConfig;
import com.monk.commondas.cache.DataConfigCache;
import com.monk.commondas.cache.TableConfig;
import com.monk.commondas.common.BIZErrorCode;
import com.monk.commondas.constant.DasConstant;
import com.monk.commondas.constant.DbAdapterServiceType;
import com.monk.commondas.exception.ValidateException;
import com.monk.commondas.util.JDBCUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDASProcessor {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractDASProcessor.class);
    
    /**
     * 当前类中的全局java时间格式化字符串
     */
    protected static final String GLOBAL_JAVA_DATE_FORMART = "yyyy-MM-dd HH:mm:ss";

    /**
     * 数据对象配置
     */
    protected String configStr;

    /**
     * 数据对象配置版本
     */
    protected String configVersion;

    /**
     * 服务英文名
     */
    protected String serviceNameEn;

    /**
     * 服务主版本号
     */
    protected String majorVersion;

    /**
     * 操作类型
     */
    protected DbAdapterServiceType operationType;
    
    /**
     * 数据库类型
     */
    protected String dataSourceType;

    /**
     * 数据库连接对象
     */
    protected Connection conn;

    /**
     * 构造方法
     * 
     * @param configStr
     *            数据对象配置信息
     * @param configVersion
     *            配置信息版本号
     * @param serviceNameEn
     *            服务英文名
     * @param majorVersion
     *            服务主版本号
     * @author Monk
     * @date 2020年8月6日 下午4:53:32
     */
    public AbstractDASProcessor(String configStr, String configVersion, String serviceNameEn,
            String majorVersion) {
        this.configStr = configStr;
        this.configVersion = configVersion;
        this.serviceNameEn = serviceNameEn;
        this.majorVersion = majorVersion;
    }

    /**
     * 将DataObject对象转换为DataConfig对象
     * 
     * @return DataConfig对象
     * @author Monk
     * @date 2020年8月6日 下午4:51:33
     */
    protected DataConfig dataObjectConvertToDataConfig() {
        DataConfig dataConfig = new DataConfig();
        dataConfig.setConfigVersion(configVersion);
        DataObjectResolver resolver = new DataObjectResolver();
        DataObject dataObject = resolver.resolveDataObject(configStr);
        if (null != dataObject) {
            dataConfig.setServiceNameEn(serviceNameEn);
            dataConfig.setMajorVersion(majorVersion);
            dataConfig.setConfigVersion(configVersion);
            dataConfig.setDataSourceName(dataObject.getDataSourceName());
            dataConfig.setDataSourceType(dataObject.getDataSourceType());
            dataConfig.setOperationType(dataObject.getOperationType());
            TableConfig masterConfig = resoleveTableInfo(dataObject.getMaster());
            dataConfig.setMaster(masterConfig);
        }

        return dataConfig;
    }

    /**
     * 将数据对象中的Table对象转换为缓存对象中的TableConfig对象
     * 
     * @param table
     *            数据对象中的Table对象
     * @return TableConfig对象
     * @author Monk
     * @date 2020年8月6日 下午4:50:22
     */
    private TableConfig resoleveTableInfo(Table table) {
        TableConfig tableConfig = new TableConfig();
        tableConfig.setEntityName(table.getEntityName());
        tableConfig.setTableName(table.getTableName());
        tableConfig.setTableType(table.getTableType());

        List<Field> fieldList = table.getFields();
        List<Field> resultList = new ArrayList<Field>();
        List<Field> importList = new ArrayList<Field>();
        List<Field> percizeList = new ArrayList<Field>();
        List<Field> fuzzyList = new ArrayList<Field>();
        List<Field> primaryList = new ArrayList<Field>();
        if (CollectionUtils.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                if (field.getPrecise()) {
                    percizeList.add(field);
                }
                if (field.getFuzzy()) {
                    fuzzyList.add(field);
                }
                if (field.getResult()) {
                    resultList.add(field);
                }
                if (field.getImport()) {
                    importList.add(field);
                }
                if (field.getPrimary()) {
                    primaryList.add(field);
                }
            }
        }
        tableConfig.setPercizeList(percizeList);
        tableConfig.setFuzzyList(fuzzyList);
        tableConfig.setResultList(resultList);
        tableConfig.setImportList(importList);
        tableConfig.setFieldList(fieldList);
        tableConfig.setPrimaryKeys(primaryList);

        List<Map<String, String>> mappingList = new ArrayList<Map<String, String>>();
        List<Key> keyList = table.getKeys();
        if (CollectionUtils.isNotEmpty(keyList)) {
            for (Key key : keyList) {
                Map<String, String> mapping = new HashMap<String, String>();
                mapping.put(key.getParentField(), key.getChildField());
                mappingList.add(mapping);
            }
        }
        tableConfig.setForeignKeys(mappingList);

        List<Table> slaveTables = table.getSlaveTables();
        List<TableConfig> slaves = new ArrayList<TableConfig>();
        if (CollectionUtils.isNotEmpty(slaveTables)) {
            for (Table slave : slaveTables) {
                TableConfig slaveConfig = resoleveTableInfo(slave);
                slaves.add(slaveConfig);
            }
        }
        tableConfig.setSlaves(slaves);

        // 生成基础SQL并放到缓存中，避免每次都去生成SQL
        buildBasicSql(tableConfig);

        return tableConfig;
    }

    /**
     * 获取缓存中的数据对象配置信息
     * 
     * @return 数据对象配置信息
     * @author Monk
     * @date 2020年8月6日 下午4:54:07
     */
    private synchronized DataConfig getConfig() {
        String cacheKey = DataConfigCache.buildCacheKey(serviceNameEn, majorVersion);
        DataConfig config = DataConfigCache.get(cacheKey);
        // 如果缓存中的服务配置信息为空，或者配置信息版本号不一致，需要重新解析配置信息，并重新put到缓存中
        if (null == config || !config.getConfigVersion().equals(configVersion)) {
            config = dataObjectConvertToDataConfig();
            if (null != config) {
                DataConfigCache.put(cacheKey, config);
                return config;
            } else {
                return null;
            }
        } else {
            // 返回配置
            return config;
        }
    }

    /**
     * 根据表配置信息生成where条件语句以前的基础SQL
     * 
     * @param tableConfig
     *            表配置信息
     * @return 基础SQL
     * @author Monk
     * @date 2020年8月6日 下午6:34:37
     */
    protected abstract void buildBasicSql(TableConfig tableConfig);

    /**
     * 校验入参
     * 
     * @param dataConfig
     *            数据对象配置信息
     * @param jsonObject
     *            inputJson入参对象
     * @author Monk
     * @date 2020年8月22日 上午10:46:17
     */
    protected abstract void validateInputJson(DataConfig dataConfig, JSONObject jsonObject);

    /**
     * 检查数据源信息是否可用
     * 
     * @param dataSourceName
     * @return
     * @author Monk
     * @date 2020年8月7日 上午10:54:21
     */
    protected Boolean verifyDataSource(String dataSourceName) {
        try {
            /* Properties properties = new Properties();
            properties.put("url", "jdbc:oracle:thin:@10.204.105.127:1521:db11g02");
            properties.put("driverClassName", "oracle.jdbc.driver.OracleDriver");
            properties.put("username", "CSBHDDEV");
            properties.put("password", "AAaa1234");
            properties.put("dataSourceName", "TestDataSource1");
            conn = JDBCUtil.getConnection(dataSourceName, properties);*/
            
            /*Properties properties = new Properties();
            properties.put("url", "jdbc:mysql://10.204.105.132:3306/zyh_test_database?rewriteBatchedStatements=true&allowMultiQueries=true");
            properties.put("driverClassName", "com.mysql.jdbc.Driver");
            properties.put("username", "root");
            properties.put("password", "AAaa1234");
            properties.put("dataSourceName", "TestDataSource2");
            conn = JDBCUtil.getConnection(dataSourceName, properties);*/

            conn = JDBCUtil.getConnection(dataSourceName);
            if (null == conn) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("get connection faield.");
            return false;
        }
    }

    /**
     * 接口处理逻辑
     * 
     * @param inputJson
     *            入参
     * @return 处理结果
     * @author Monk
     * @throws SQLException
     * @date 2020年8月7日 下午8:23:36
     */
    public OutputParameters process(String inputJson) throws SQLException {
        OutputParameters output = new OutputParameters();
        //output.setESBFLAG(DasConstant.SUCCESS_FLAG);
        DataConfig dataConfig = getConfig();
        
        if (logger.isDebugEnabled()) {
            logger.debug("serviceNameEn:{}, majorVersion:{}, configVersion:{}, config:{}",
                    new Object[] { serviceNameEn, majorVersion, configVersion, dataConfig });
        }
        // 获取服务配置信息
        if (null == dataConfig) {
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.BIZ_ERROR.getCode());
            output.setBIZRETURNMESSAGE(BIZErrorCode.BIZ_ERROR.getMsg() + "：解析服务配置信息失败");
            return output;
        }

        // 校验操作类型和服务名称是否匹配
        if (!StringUtils.equals(operationType.getTypeEn(), dataConfig.getOperationType())) {
            logger.error("operationType:{}, dataConfig's operationType:{}", operationType.getTypeEn(),
                    dataConfig.getOperationType());
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.BIZ_ERROR.getCode());
            output.setBIZRETURNMESSAGE(BIZErrorCode.BIZ_ERROR.getMsg() + "：操作类型与服务名称不匹配");
            return output;
        }

        // 校验数据源是否可用
        if (!verifyDataSource(dataConfig.getDataSourceName())) {
            logger.error("The datasource [{}] is invalid.");
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.BIZ_ERROR.getCode());
            output.setBIZRETURNMESSAGE(
                    BIZErrorCode.BIZ_ERROR.getMsg() + "：数据源【" + dataConfig.getDataSourceName() + "】不可用");
            return output;
        }

        // 校验入参是是否合法
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(inputJson);
            if(null == jsonObject) {
                logger.error("The inputJson is null.");
                output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
                output.setBIZRETURNCODE(BIZErrorCode.BIZ_ERROR.getCode());
                output.setBIZRETURNMESSAGE(
                        BIZErrorCode.BIZ_ERROR.getMsg() + "：入参JSON对象不可为空");
                return output;
            }
        } catch (Exception e) {
            logger.error("Failed to parse json object. {}", e);
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.VALIDATE_ERROR.getCode());
            output.setBIZRETURNMESSAGE(BIZErrorCode.VALIDATE_ERROR.getMsg() + "：InputJson不合法");
            return output;
        }

        try {
            validateInputJson(dataConfig, jsonObject);
        } catch (ValidateException e) {
            logger.error("validate faield. {}", e.getMessage());
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.VALIDATE_ERROR.getCode());
            output.setBIZRETURNMESSAGE(BIZErrorCode.VALIDATE_ERROR.getMsg() + "：" + e.getMessage());
            return output;
        }

        Map<String, Object> executeResult = new HashMap<String, Object>(0);
        dataSourceType = dataConfig.getDataSourceType();
        try {
            conn.setAutoCommit(false);
            executeResult = executeSql(dataConfig, jsonObject);
            conn.commit();
        } catch (ValidateException ve) {
            conn.rollback();
            logger.error("validate failed. {}", ve.getMessage());
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.VALIDATE_ERROR.getCode());
            output.setBIZRETURNMESSAGE(BIZErrorCode.VALIDATE_ERROR.getMsg() + ". " + ve.getMessage());
            return output;
        } catch (Exception e) {
            conn.rollback();
            logger.error("execute sql error. {}", e);
            output.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            output.setBIZRETURNCODE(BIZErrorCode.OTHER_ERROR.getCode());
            output.setBIZRETURNMESSAGE(BIZErrorCode.OTHER_ERROR.getMsg() + ". " + e.getMessage());
            return output;
        } finally {
            JDBCUtil.closeConnection(conn);
        }

        output.setBIZSERVICEFLAG(DasConstant.SUCCESS_FLAG);
        output.setBIZRETURNMESSAGE(operationType.getTypeCn() + "成功");
        output.setTOTALRECORD(new BigDecimal(executeResult.size()));
        output.setOUTPUTJSON(JSON.toJSONStringWithDateFormat(executeResult, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[] {
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue, 
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullListAsEmpty}));

        return output;
    }

    /**
     * 执行SQL并返回结果
     * 
     * @param dataConfig
     *            数据对象配置信息
     * @param jsonObject
     *            入参对象
     * @return SQL执行结果
     * @throws Exception
     * @author Monk
     * @date 2020年8月7日 下午8:25:09
     */
    public abstract Map<String, Object> executeSql(DataConfig dataConfig, JSONObject jsonObject);

    public String getConfigStr() {
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public DbAdapterServiceType getOperationType() {
        return operationType;
    }

    public void setOperationType(DbAdapterServiceType operationType) {
        this.operationType = operationType;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

}
