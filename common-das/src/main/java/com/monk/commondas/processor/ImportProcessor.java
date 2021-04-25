package com.monk.commondas.processor;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.monk.commondas.constant.DasConstant;
import com.monk.commondas.exception.FieldIllegalException;
import com.monk.commondas.exception.ValidateException;
import com.monk.commondas.util.DasUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.monk.commondas.bean.Field;
import com.monk.commondas.cache.DataConfig;
import com.monk.commondas.cache.TableConfig;
import com.monk.commondas.constant.DbAdapterServiceType;
import com.monk.commondas.exception.FieldRequirelException;
import com.monk.commondas.util.DasJdbcUtil;

public class ImportProcessor extends AbstractDASProcessor {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ImportProcessor.class);

    /**
     * 操作类型：插入/更新
     */
    private static final String OPERRATION_INSERT = "INSERT";
    private static final String OPERRATION_UPDATE = "UPDATE";

    /**
     * 构造方法
     * 
     * @param configStr
     *            表配置信息
     * @param configVersion
     *            配置信息版本号
     * @param serviceNameEn
     *            服务英文名
     * @param majorVersion
     *            服务主版本号
     * @author Monk
     * @date 2020年8月15日 上午11:57:31
     */
    public ImportProcessor(String configStr, String configVersion, String serviceNameEn, String majorVersion) {
        super(configStr, configVersion, serviceNameEn, majorVersion);
        super.operationType = DbAdapterServiceType.IMPORT;
    }

    @Override
    protected void buildBasicSql(TableConfig tableConfig) {
        /**
         * 由于导入服务的逻辑相对复杂，需要先判断导入的数据是否存在，再进行导入，<br/>
         * 故无法像查询和删除一样，有固定的SQL，这里需要动态的生成SQL
         */
    }

    /**
     * 校验逻辑：</br>
     * 1. 导入服务中的"主键"、"导入"字段均需要在input_json中传值;</br>
     * 2. 主从关联关系的值要保持一致
     */
    @Override
    protected void validateInputJson(DataConfig dataConfig, JSONObject jsonObject) {
        TableConfig master = dataConfig.getMaster();
        Object masterValue = jsonObject.get(master.getEntityName());

        // 1. 校验 导入服务中的"主键"、"导入"字段均需要在input_json中传值
        List<Field> validateFieldList = new ArrayList<Field>(0);
        List<Field> primaryFieldList = master.getPrimaryKeys();
        List<Field> importFieldList = master.getImportList();
        validateFieldList.addAll(primaryFieldList);
        validateFieldList.addAll(importFieldList);
        if (CollectionUtils.isNotEmpty(validateFieldList)) {
            if (null == masterValue) {
                String msg = String.format("The input_json propertie master tables [%s] is require.",
                        master.getEntityName());
                logger.error("{}", msg);
                throw new FieldRequirelException(msg);
            }

            for (Field field : validateFieldList) {
                validateProperties(master, field, masterValue);
            }
        }

        // 2. 校验 主从关联关系的值要保持一致
        List<TableConfig> slaveTableList = master.getSlaves();
        if (CollectionUtils.isNotEmpty(slaveTableList)) {
            for (TableConfig slave : slaveTableList) {
                List<Map<String, String>> foreignKeys = slave.getForeignKeys();
                if (CollectionUtils.isNotEmpty(foreignKeys)) {
                    for (Map<String, String> map : foreignKeys) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String parentFiledName = entry.getKey();
                            String currFieldName = entry.getValue();
                            Field parentField = DasUtil.getFieldByColumnName(parentFiledName,
                                    master.getFieldList());
                            Field currField = DasUtil.getFieldByColumnName(currFieldName, slave.getFieldList());
                            validateMasterSlaveRelationFieldValue(masterValue, master.getEntityName(),
                                    parentField.getFieldName(), slave.getEntityName(), currField.getFieldName());
                        }
                    }
                }
            }
        }
    }

    /**
     * 校验主从关联关系字段值
     * 
     * @param parentValue
     *            主表值
     * @param parentElementName
     *            主表节点名称
     * @param parentFieldName
     *            主表关联字段名称
     * @param childElementName
     *            从表节点名称
     * @param childFieldName
     *            从表关联字段名称
     * @author Monk
     * @date 2020年8月27日 下午11:22:54
     */
    private void validateMasterSlaveRelationFieldValue(Object parentValue, String parentElementName,
            String parentFieldName, String childElementName, String childFieldName) {
        if (parentValue instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) parentValue;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                compareValue(jsonObj, parentElementName, parentFieldName, childElementName, childFieldName);
            }
        } else if (parentValue instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) parentValue;

            compareValue(jsonObj, parentElementName, parentFieldName, childElementName, childFieldName);
        }
    }

    /**
     * 比较值是否相等
     * 
     * @param jsonObj
     *            主节点值
     * @param parentElementName
     *            主表节点名称
     * @param parentFieldName
     *            主表关联字段名称
     * @param childElementName
     *            从表节点名称
     * @param childFieldName
     *            从表关联字段名称
     * @author Monk
     * @date 2020年8月27日 下午11:23:40
     */
    private void compareValue(JSONObject jsonObj, String parentElementName, String parentFieldName,
            String childElementName, String childFieldName) {
        Object parentFieldValue = jsonObj.get(parentFieldName);

        Object childElementValue = jsonObj.get(childElementName);
        if (childElementValue instanceof JSONArray) {
            JSONArray childArray = (JSONArray) childElementValue;
            for (int j = 0; j < childArray.size(); j++) {
                JSONObject childFieldValue = childArray.getJSONObject(j);
                Object childValue = childFieldValue.get(childFieldName);
                if (!parentFieldValue.equals(childValue)) {
                    String msg = String.format(
                            "The 【%s.%s】(value:%s) attribute of the slave table is not equal to the 【%s.%s】(value:%s) attribute of the master table",
                            new Object[] { childElementName, childFieldName, childValue, parentElementName,
                                    parentFieldName, parentFieldValue });
                    logger.error("{}", msg);
                    throw new FieldIllegalException(msg);
                }
            }
        } else if (childElementValue instanceof JSONObject) {
            JSONObject child = (JSONObject) childElementValue;
            Object childValue = child.get(childFieldName);
            if (!parentFieldValue.equals(childValue)) {
                String msg = String.format(
                        "The 【%s.%s】(value:%s) attribute of the slave table is not equal to the 【%s.%s】(value:%s) attribute of the master table",
                        new Object[] { childElementName, childFieldName, childValue, parentElementName,
                                parentFieldName, parentFieldValue });
                logger.error("{}", msg);
                throw new FieldIllegalException(msg);
            }
        } else {
            logger.error("Unknown json type. value:[{}]", childElementValue);
            throw new FieldIllegalException("Unknown json type.");
        }
    }

    /**
     * 校验入参字段是否合法
     * 
     * @param entityName
     *            入参实体名称
     * @param field
     *            字段对象
     * @param object
     *            入参object值
     * @author Monk
     * @date 2020年8月22日 上午11:49:57
     */
    private void validateProperties(TableConfig table, Field field, Object object) {
        String fieldName = field.getFieldName();
        if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Object value = jsonObj.get(fieldName);

                if (null == value || StringUtils.isBlank(String.valueOf(value))) {
                    if (null == field.getRequire() || field.getRequire()) {
                        value = field.getDefaultValue();
                    }
                }

                DasUtil.validateFieldValue(table.getEntityName(), field, value);

                // 校验从表
                validateSlaveFields(table, jsonObj);
            }
        } else {
            Object value = ((JSONObject) object).get(fieldName);

            if (null == value || StringUtils.isBlank(String.valueOf(value))) {
                if (null == field.getRequire() || field.getRequire()) {
                    value = field.getDefaultValue();
                }
            }

            DasUtil.validateFieldValue(table.getEntityName(), field, value);

            // 校验从表
            validateSlaveFields(table, (JSONObject) object);
        }
    }

    /**
     * 校验从表字段
     * 
     * @param table
     *            表配置信息
     * @param jsonObj
     *            表入参值
     * @author Monk
     * @date 2020年8月22日 上午11:56:48
     */
    private void validateSlaveFields(TableConfig table, JSONObject jsonObj) {
        List<TableConfig> slaveTableList = table.getSlaves();
        if (CollectionUtils.isNotEmpty(slaveTableList)) {
            for (TableConfig slave : slaveTableList) {
                List<Field> primaryFieldList = slave.getPrimaryKeys();
                List<Field> importFieldList = slave.getImportList();
                List<Field> validateFieldList = new ArrayList<Field>(0);
                validateFieldList.addAll(primaryFieldList);
                validateFieldList.addAll(importFieldList);
                if (CollectionUtils.isNotEmpty(validateFieldList)) {
                    Object slaveValue = jsonObj.get(slave.getEntityName());
                    if (null == slaveValue) {
                        String msg = String.format("The input_json propertie salve tables [%s] is require.",
                                slave.getEntityName());
                        throw new FieldRequirelException(msg);
                    }
                    for (Field slaveField : validateFieldList) {
                        validateProperties(slave, slaveField, slaveValue);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> executeSql(DataConfig dataConfig, JSONObject jsonObject) {
        /**
         * 主表操作逻辑： 根据主表中的维护的主键字段信息，先判断数据是否存在，存在就update，不存在就insert 从表操作逻辑：
         * 根据从表中的维护的主键字段信息 + 主从关联关系字段，先判断数据是否存在，存在就update，不存在就insert
         */
        TableConfig master = dataConfig.getMaster();
        Object masterObject = jsonObject.get(master.getEntityName());
        if (null == masterObject) {
            throw new ValidateException("主表入参为空");
        }
        processForTable(master, null, masterObject, null);

        return new HashMap<String, Object>(0);
    }

    /**
     * 处理单张表数据入库
     * 
     * @param currTable
     *            当前表
     * @param parentTable
     *            主表
     * @param currObject
     *            当前表入参对象
     * @param parentObject
     *            主表入参对象
     * @author Monk
     * @date 2020年8月21日 下午4:46:01
     */
    private void processForTable(TableConfig currTable, TableConfig parentTable, Object currObject,
            Object parentObject) {
        if (currObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) currObject;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject value = jsonArray.getJSONObject(i);
                processSingleObject(currTable, parentTable, value, (JSONObject) parentObject);

                // 处理从表
                List<TableConfig> slaveList = currTable.getSlaves();
                for (TableConfig slave : slaveList) {
                    Object slaveObject = value.get(slave.getEntityName());
                    if (null == slaveObject) {
                        logger.warn("input_json {} is null. skip insert data to this table[{}].",
                                slave.getEntityName(), slave.getTableName());
                        continue;
                    }
                    processForTable(slave, currTable, slaveObject, value);
                }
            }
        } else if (currObject instanceof JSONObject) {
            JSONObject value = (JSONObject) currObject;
            processSingleObject(currTable, parentTable, value, (JSONObject) parentObject);

            // 处理从表
            List<TableConfig> slaveList = currTable.getSlaves();
            for (TableConfig slave : slaveList) {
                Object slaveObject = value.get(slave.getEntityName());
                processForTable(slave, currTable, slaveObject, value);
            }
        } else {
            logger.warn("unknown type for object. {}", currObject);
        }

    }

    /**
     * 处理单个数据对象
     * 
     * @param currTable
     *            当前表
     * @param parentTable
     *            主表
     * @param currObject
     *            当前表入参对象
     * @param parentObject
     *            主表入参对象
     * @author Monk
     * @date 2020年8月21日 下午4:46:59
     */
    private void processSingleObject(TableConfig currTable, TableConfig parentTable, JSONObject currValue,
            JSONObject parentValue) {
        Boolean existFlag = existData(currTable, parentTable, currValue, parentValue);
        if (existFlag) {
            update(currTable, parentTable, currValue, parentValue);
        } else {
            insert(currTable, parentTable, currValue, parentValue);
        }
    }

    /**
     * insert操作
     * 
     * @param config
     *            当前表配置信息
     * @param parentConfig
     *            父表配置信息
     * @param object
     *            当前表入参json对象信息
     * @param parentObject
     *            父表入参json对象信息
     * @return 参数值
     * @author Monk
     * @date 2020年8月15日 上午11:54:46
     */
    private void insert(TableConfig config, TableConfig parentConfig, JSONObject object, JSONObject parentObject) {
        String insertSql = buildInsertSql(config);
        if (StringUtils.isBlank(insertSql)) {
            logger.warn("{}'s insert sql is empty.", config.getTableName());
            return;
        }
        List<Object> params = buildPlaceholderValues(config, parentConfig, object, parentObject, OPERRATION_INSERT);
        try {
            logger.info("insert sql:[{}], params:{}", insertSql, params.toArray());
            DasJdbcUtil.executeBatchNativeSql(conn, insertSql, params.toArray());
        } catch (SQLException e) {
            logger.error("execute insert sql failed, sql:[{}], params:[{}]", insertSql, params);
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建占位符所在的参数值
     * 
     * @param config
     *            当前表配置信息
     * @param parentConfig
     *            父表配置信息
     * @param object
     *            当前表入参json对象信息
     * @param parentObject
     *            父表入参json对象信息
     * @return 参数值
     * @author Monk
     * @date 2020年8月15日 上午11:54:46
     */
    private List<Object> buildPlaceholderValues(TableConfig config, TableConfig parentConfig, JSONObject object,
            JSONObject parentObject, String type) {
        List<Object> params = new ArrayList<Object>();
        List<Field> fieldList = mergeUpdateField(config, type);

        if (CollectionUtils.isNotEmpty(fieldList)) {

            //List<Map<String, String>> foreignKeyList = config.getForeignKeys();

            for (Field field : fieldList) {
                Object value = object.get(field.getFieldName());

                //如果主从关联的值从父表中取，就放开下面这段代码
                /* String columnName = field.getColumnName();
                for (Map<String, String> map : foreignKeyList) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (columnName.equals(entry.getValue())) {
                            String parentColumnName = entry.getKey();
                            Field parentField = DasUtil.getFieldByColumnName(parentColumnName,
                                    parentConfig.getFieldList());
                            value = parentObject.get(parentField.getFieldName());
                        }
                    }
                }*/

                String defaultValue = field.getDefaultValue();
                value = (null == value || StringUtils.isBlank(String.valueOf(value))) ? defaultValue : value;
                String columnType = field.getColumnType();
                if (columnType.toUpperCase().contains("DATE")) {
                    String fieldConstraint = field.getFieldConstraint();
                    if (DasConstant.DATABASE_TYPE_SQLSERVER.equalsIgnoreCase(dataSourceType)) {
                        fieldConstraint = GLOBAL_JAVA_DATE_FORMART;
                    } else {
                        if (DasConstant.DEFAULT_FIELD_CONSTRAINT.equals(fieldConstraint)) {
                            String str = String.valueOf(value).replaceAll("[- :]", "");
                            if (str.length() < 14) {
                                value = String.format("%-14s", str).replace(' ', '0');
                            } else {
                                value = str;
                            }
                        }
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat(fieldConstraint);
                    Date date = null;
                    try {
                        date = sdf.parse(String.valueOf(value));
                        value = sdf.format(date);
                    } catch (ParseException e) {
                        logger.error("Failed to build placeholder values. value:[{}], file constraint:[{}] ",
                                value, fieldConstraint);
                        throw new RuntimeException(e);
                    }
                } else if (columnType.toUpperCase().contains("TIMESTAMP")) {
                    Long timestamp = Long.valueOf((String) value);
                    Date dateTimestamp = new Date(timestamp * 1000);
                    SimpleDateFormat sdf = new SimpleDateFormat(GLOBAL_JAVA_DATE_FORMART);
                    value = sdf.format(dateTimestamp);
                }

                params.add(value);
            }
        }
        return params;
    }

    /**
     * 生成update语句
     * 
     * @param config
     *            表配置信息
     * @return update语句
     * @author Monk
     * @date 2020年8月15日 上午11:53:42
     */
    private String buildUpdateSql(TableConfig config) {
        // update by 2020-12-02 update时，set后的字段应该过滤掉主键字段
        List<Field> updateFieldList = mergeUpdateField(config, OPERRATION_UPDATE);
        StringBuffer updateSql = new StringBuffer();
        if (CollectionUtils.isNotEmpty(updateFieldList)) {
            updateSql.append("UPDATE ").append(config.getTableName()).append(" ");
            String updateColumns = buildUpdateColumns(updateFieldList);
            if (StringUtils.isNotBlank(updateColumns)) {
                updateSql.append(updateColumns);
            }
        }
        return updateSql.toString();
    }

    /**
     * 合并要更新的字段，去掉主键
     * 
     * @param config
     *            表配置信息
     * @return 合并后的更新字段集合
     * @author Monk
     * @date 2020年12月2日 上午9:25:59
     */
    private List<Field> mergeUpdateField(TableConfig config, String type) {
        List<Field> resultList = new ArrayList<Field>();
        List<Field> importFieldList = config.getImportList();
        List<Field> primaryFieldList = config.getPrimaryKeys();

        if (CollectionUtils.isNotEmpty(importFieldList)) {
            for (Field importField : importFieldList) {
                // sqlserver数据库的自增长字段不支持更新/插入，会直接报错，mysql的自增长字段支持更新/插入
                if (DasConstant.DATABASE_TYPE_SQLSERVER.equalsIgnoreCase(dataSourceType)
                        && importField.getIdentity()) {
                    continue;
                }

                // 更新的时候不仅仅要剔除自增长字段，还要排除主键字段，更新主键字段没有意义
                if (OPERRATION_UPDATE.equals(type) && CollectionUtils.isNotEmpty(primaryFieldList)) {
                    boolean flag = false;
                    for (Field primaryField : primaryFieldList) {
                        if (importField.getFieldName().equals(primaryField.getFieldName())) {
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) {
                        resultList.add(importField);
                    }
                }else if (OPERRATION_INSERT.equals(type)) {
                    resultList.add(importField);
                }
            }
        }
        return resultList;
    }

    /**
     * 构建update语句字段
     * 
     * @param fieldList
     *            要更新的字段集合
     * @return update语句字段
     * @author Monk
     * @date 2020年8月6日 下午6:23:30
     */
    private String buildUpdateColumns(List<Field> fieldList) {
        StringBuilder builder = new StringBuilder();
        Boolean firstFlag = true;
        if (CollectionUtils.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                if (firstFlag) {
                    builder.append("SET ");
                }
                builder.append(field.getColumnName()).append(" = ");
                builder.append(buildPlaceholder(field));
                firstFlag = false;
            }
        }
        String result = builder.toString().trim();
        if (StringUtils.isNotBlank(result) && result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;

    }

    /**
     * 生成insert语句
     * 
     * @param config
     *            表配置信息
     * @return insert语句
     * @author Monk
     * @date 2020年8月15日 上午11:53:20
     */
    private String buildInsertSql(TableConfig config) {
        String insertSql = "INSERT INTO %s (%s) VALUES (%s)";
        List<Field> fieldList = mergeUpdateField(config, OPERRATION_INSERT);
        String basicSql = "";
        if (CollectionUtils.isNotEmpty(fieldList)) {
            String importColumns = buildInsertColumns(fieldList);
            String importPlaceholder = buildPlaceholder(fieldList);
            if (StringUtils.isNotBlank(importColumns)) {
                basicSql = String.format(insertSql, config.getTableName(), importColumns, importPlaceholder);
            }
        }
        return basicSql;
    }

    /**
     * 构建插入的字段列
     * 
     * @param fieldList
     *            插入的字段集合
     * @return 插入的字段列
     * @author Monk
     * @date 2020年8月6日 下午6:19:36
     */
    private String buildInsertColumns(List<Field> fieldList) {
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                builder.append(field.getColumnName()).append(",");
            }
        }
        String result = builder.toString();
        if (StringUtils.isNotBlank(result) && result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 构建插入的字段列的占位符
     * 
     * @param num
     *            占位符个数
     * @return 构建插入的字段列的占位符
     * @author Monk
     * @date 2020年8月6日 下午6:19:57
     */
    private String buildPlaceholder(List<Field> fieldList) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            builder.append(buildPlaceholder(field));
            /*if(DasConstant.DATABASE_TYPE_ORACLE.equalsIgnoreCase(dataSourceType)) {
                builder.append(buildPlaceholder(field));
            }else {
                builder.append("?, ");
            }*/
        }

        String result = builder.toString().trim();
        if (StringUtils.isNotBlank(result) && result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String buildPlaceholder(Field field) {
        String result = "";
        String columnType = field.getColumnType();
        if (DasConstant.DATABASE_TYPE_ORACLE.equalsIgnoreCase(dataSourceType)) {
            if (columnType.toUpperCase().contains("DATE")) {
                result = "TO_DATE(?, 'yyyy-mm-dd hh24:mi:ss'), ";
            } else if (columnType.toUpperCase().contains("TIMESTAMP")) {
                result = "TO_TIMESTAMP(?, 'yyyy-mm-dd hh24:mi:ss'), ";
            } else {
                result = "?, ";
            }
        } else if (DasConstant.DATABASE_TYPE_MYSQL.equalsIgnoreCase(dataSourceType)) {
            if (columnType.toUpperCase().contains("DATE") || columnType.toUpperCase().contains("TIMESTAMP")) {
                result = "DATE_FORMAT(?, '%Y-%m-%d %H:%i:%S'), ";
            } else {
                result = "?, ";
            }
        } else {
            result = "?, ";
        }
        return result;
    }

    /**
     * 更新操作
     * 
     * @param config
     *            当前表配置信息
     * @param parentConfig
     *            父表配置信息
     * @param jsonObject
     *            当前表入参json对象
     * @param parentObject
     *            父表入参json对象
     * @author Monk
     * @date 2020年8月14日 下午5:16:52
     */
    @SuppressWarnings("unchecked")
    private void update(TableConfig config, TableConfig parentConfig, JSONObject jsonObject,
            JSONObject parentObject) {
        StringBuffer updateSql = new StringBuffer();
        String updateBasicSql = buildUpdateSql(config);
        if (StringUtils.isBlank(updateBasicSql)) {
            logger.warn("{}'s update sql is empty.", config.getTableName());
            return;
        }
        updateSql.append(updateBasicSql);
		List<Object> params = buildPlaceholderValues(config, parentConfig, jsonObject, parentObject, OPERRATION_UPDATE);

        Map<String, Object> conditionMap = DasUtil.buildWhereCondition(config, parentConfig, jsonObject,
                parentObject, true);
        String whereSql = MapUtils.getString(conditionMap, "sql");
        List<Object> whereParams = (List<Object>) MapUtils.getObject(conditionMap, "params");
        updateSql.append(whereSql);
        params.addAll(whereParams);
        try {
            logger.info("update sql:[{}], params:{}", updateSql, params.toArray());
            DasJdbcUtil.executeBatchNativeSql(conn, updateSql.toString(), params.toArray());
        } catch (SQLException e) {
            logger.error("execute update sql failed, sql:[{}], params:[{}]", updateSql, params);
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断插入的数据是否存在
     * 
     * @param config
     *            当前表配置信息
     * @param parentConfig
     *            父表配置信息
     * @param jsonObject
     *            当前表入参json对象
     * @param parentObject
     *            父表入参json对象
     * @return 数据是否存在
     * @author Monk
     * @date 2020年8月14日 下午5:16:52
     */
    @SuppressWarnings("unchecked")
    private Boolean existData(TableConfig config, TableConfig parentConfig, JSONObject jsonObject,
            JSONObject parentObject) {
        StringBuffer selectSql = new StringBuffer("SELECT COUNT(1) FROM ");
        selectSql.append(config.getTableName());
        Map<String, Object> conditionMap = DasUtil.buildWhereCondition(config, parentConfig, jsonObject,
                parentObject, true);
        String whereSql = MapUtils.getString(conditionMap, "sql");
        List<Object> params = (List<Object>) MapUtils.getObject(conditionMap, "params");
        selectSql.append(whereSql);
        try {
            logger.info("exist sql:[{}], params:{}", selectSql, params.toArray());
            Object number = DasJdbcUtil.queryUniqueResult(conn, selectSql.toString(), params.toArray());
            int num = 0;
            if (number instanceof Long) {
                // oracle Mysql数据库查询返回的数据类型是Long
                num = ((Long) number).intValue();
            } else if (number instanceof Integer) {
                // sqlserver数据库查询返回的数据类型是Integer，直接强转任何一个类型都会在不通的数据源类型下会报错
                num = ((Integer) number).intValue();
            }
            if (num > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.error("execute sql :{}, params:[{}]", new Object[] { selectSql, params });
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
    }

}
