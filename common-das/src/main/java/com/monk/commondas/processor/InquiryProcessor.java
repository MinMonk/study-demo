package com.monk.commondas.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.monk.commondas.cache.DataConfig;
import com.monk.commondas.cache.TableConfig;
import com.monk.commondas.exception.FieldIllegalException;
import com.monk.commondas.util.DasUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.monk.commondas.bean.Field;
import com.monk.commondas.constant.DbAdapterServiceType;
import com.monk.commondas.util.DasJdbcUtil;

public class InquiryProcessor extends AbstractDASProcessor {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(InquiryProcessor.class);

    /**
     * 基础查询SQL模板
     */
    private static String SELECT_SQL_FORMAT = "SELECT %s FROM %s";

    public InquiryProcessor(String configStr, String configVersion, String serviceNameEn, String majorVersion) {
        super(configStr, configVersion, serviceNameEn, majorVersion);
        super.operationType = DbAdapterServiceType.INQUIRY;
    }

    /**
     * 生成查询SQL查询结果列：勾选的查询结果列 + 主从关联关系的列
     */
    @Override
    public void buildBasicSql(TableConfig tableConfig) {

        String basicSql = "";
        List<Field> resultFieldList = new ArrayList<Field>(0);
        resultFieldList.addAll(tableConfig.getResultList());
        List<TableConfig> slaveTableList = tableConfig.getSlaves();
        if (CollectionUtils.isNotEmpty(slaveTableList)) {
            for (TableConfig slave : slaveTableList) {
                List<Map<String, String>> foreignKeys = slave.getForeignKeys();
                if (CollectionUtils.isNotEmpty(foreignKeys)) {
                    for (Map<String, String> map : foreignKeys) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String key = entry.getKey();
                            Field field = DasUtil.getFieldByColumnName(key, tableConfig.getFieldList());
                            if (null == field) {
                                String msg = String.format(
                                        "Not found any field info by columnName(value:%s) in %s. ", key,
                                        tableConfig.getEntityName());
                                logger.error("{}", msg);
                                throw new RuntimeException(msg);
                            }
                            resultFieldList.add(field);
                        }
                    }
                }
            }
        }

        resultFieldList = DasUtil.removeDuplicateField(resultFieldList);
        if (CollectionUtils.isNotEmpty(resultFieldList)) {
            String selectColumns = buildResultColumn(resultFieldList);
            if (StringUtils.isNotBlank(selectColumns)) {
                basicSql = String.format(SELECT_SQL_FORMAT, selectColumns, tableConfig.getTableName());
            }
        } else {
            logger.warn("The select fields is null.");
            throw new RuntimeException("该服务的查询字段为空，请重新发布该服务");
        }

        logger.info("select basic sql:{}", basicSql);
        if (logger.isDebugEnabled()) {
            logger.debug("select basic sql:{}", basicSql);
        }
        tableConfig.setBasicSql(basicSql);
    }

    /**
     * 构建查询字段
     * 
     * @param fieldList
     *            查询字段
     * @return 查询字段
     * @author Monk
     * @date 2020年8月6日 下午5:55:08
     */
    private String buildResultColumn(List<Field> fieldList) {
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                builder.append(field.getColumnName()).append(" AS \"").append(field.getFieldName().toUpperCase())
                        .append("\", ");
            }
        }
        String result = builder.toString().trim();
        if (StringUtils.isNotBlank(result) && result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    @Override
    protected void validateInputJson(DataConfig dataConfig, JSONObject jsonObject) {
        TableConfig masterConfig = dataConfig.getMaster();
        Object masterValue = jsonObject.get(masterConfig.getEntityName());
        if (null != masterValue && masterValue instanceof JSONArray) {
            String msg = "Query criteria cannot be arrays.";
            logger.error("{}", msg);
            throw new FieldIllegalException(msg);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> executeSql(DataConfig dataConfig, JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 1. 先执行主表
        TableConfig masterConfig = dataConfig.getMaster();
        Object masterValue = jsonObject.get(masterConfig.getEntityName());
        if (null == masterValue) {
            return result;
        }
        String databaseType = dataConfig.getDataSourceType();
        Map<String, Object> masterCondition = buildWhereCondition(masterConfig, (JSONObject) masterValue,
                databaseType);
        String masterWhereSql = MapUtils.getString(masterCondition, "sql");
        List<Object> masterParams = (List<Object>) MapUtils.getObject(masterCondition, "params");
        String masterSql = masterConfig.getBasicSql();
        masterSql = masterSql + masterWhereSql;
        logger.info("inquiry sql:[{}], params:{}", masterSql, masterParams.toArray());
        List<Map<String, Object>> masterResult = null;
        try {
            masterResult = DasJdbcUtil.queryByNativeSql(conn, masterSql, masterParams.toArray());
        } catch (Exception e) {
            logger.error("execute select sql failed, sql:[{}], params:[{}]", masterSql.toString(),
                    masterParams.toArray());
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
        // 2. 执行从表
        if (CollectionUtils.isNotEmpty(masterResult) && CollectionUtils.isNotEmpty(masterConfig.getSlaves())) {
            for (Map<String, Object> resultMap : masterResult) {
                for (TableConfig slaveConfig : masterConfig.getSlaves()) {
                    StringBuffer slaveSql = new StringBuffer();
                    List<Object> salveParams = new ArrayList<Object>(0);
                    slaveSql.append(slaveConfig.getBasicSql());
                    Boolean firstCondition = true;
                    List<Map<String, String>> foreignKeyList = slaveConfig.getForeignKeys();
                    for (Map<String, String> map : foreignKeyList) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            Field parentField = DasUtil.getFieldByColumnName(entry.getKey(),
                                    masterConfig.getFieldList());
                            if (null == parentField) {
                                String msg = String.format(
                                        "Column 【%s】 does not exist in the master table config.", entry.getKey());
                                logger.error("{}", msg);
                                throw new RuntimeException(msg);
                            }
                            Object parentFieldValue = MapUtils.getObject(resultMap,
                                    parentField.getFieldName().toUpperCase());
                            if (null != parentFieldValue) {
                                if (!firstCondition) {
                                    slaveSql.append(" AND ");
                                }else {
                                    slaveSql.append(" WHERE ");
                                }
                                slaveSql.append(entry.getValue()).append(" = ? ");
                                salveParams.add(parentFieldValue);
                                firstCondition = false;
                            } else {
                                String msg = String.format(
                                        "Attribute 【%s】 is null in the master table query result", entry.getKey());
                                logger.error("{}", msg);
                                throw new RuntimeException(msg);
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(salveParams)) {
                        logger.info("inquiry sql:[{}], params:{}", slaveSql.toString(), salveParams.toArray());
                        List<Map<String, Object>> slaveResult = null;

                        try {
                            slaveResult = DasJdbcUtil.queryByNativeSql(conn, slaveSql.toString(),
                                    salveParams.toArray());
                        } catch (Exception e) {
                            logger.error("execute select sql failed, sql:[{}], params:[{}]", slaveSql.toString(),
                                    salveParams.toArray());
                            logger.error("{}", e);
                            throw new RuntimeException(e);
                        }
                        List<Map<String, Object>> convertSlave = convertFieldName(slaveResult,
                                slaveConfig.getResultList());
                        resultMap.put(slaveConfig.getEntityName(), convertSlave);
                    }
                }
            }
        }
        List<Map<String, Object>> convertMaster = convertFieldName(masterResult, masterConfig.getResultList());
        result.put(masterConfig.getEntityName(), convertMaster);

        return result;
    }

    /**
     * 转换查询结果集Map中的字段名称，数据库查询默认返回的字段别名是大写，这里替换为界面设置的业务字段
     * 
     * @param mapList
     *            查询结果集
     * @param fieldList
     *            Field集合
     * @return 转换FieldName后的查询结果集
     * @author Monk
     * @date 2020年8月17日 下午3:58:38
     */
    private List<Map<String, Object>> convertFieldName(List<Map<String, Object>> mapList, List<Field> fieldList) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(mapList) && CollectionUtils.isNotEmpty(fieldList)) {
            for (Map<String, Object> map : mapList) {
                Map<String, Object> temp = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof ArrayList) {
                        temp.put(entry.getKey(), entry.getValue());
                    } else {
                        Field field = getFiledByFiledName(key, fieldList);
                        if (null != field) {
                            temp.put(field.getFieldName(), entry.getValue());
                        }
                    }
                }
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * 根据字段名称获取Field对象信息
     * 
     * @param fieldName
     *            字段名称
     * @param fieldList
     *            Field集合
     * @return Field对象信息
     * @author Monk
     * @date 2020年8月17日 下午3:58:01
     */
    private Field getFiledByFiledName(String fieldName, List<Field> fieldList) {
        for (Field field : fieldList) {
            if (StringUtils.isNotBlank(field.getFieldName())) {
                if (StringUtils.equalsIgnoreCase(fieldName, field.getFieldName().toUpperCase())) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 构建查询条件
     * 
     * @param config
     *            表配置信息
     * @param jsonObject
     *            入参参数
     * @param databaseType
     *            数据源类型
     * @return 查询条件
     * @author Monk
     * @date 2020年8月17日 上午11:01:14
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildWhereCondition(TableConfig config, JSONObject jsonObject,
            String databaseType) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Field> percizeList = config.getPercizeList();
        List<Field> fuzzyList = config.getFuzzyList();

        if (null == jsonObject) {
            return result;
        }
        Map<String, Object> percizeMap = buildCondition(percizeList, jsonObject, true);
        Map<String, Object> fuzzyMap = buildCondition(fuzzyList, jsonObject, false);

        /**
         * 不同数据库where后面的条件执行顺序不一样，而模糊查询不走索引，故先精确查询后再模糊查询 </br>
         * ORACLE：从右至左 </br>
         * MYSQL：从左至右 </br>
         * SQL Sever：虽然SQL Sever引擎能帮助我们判定 WHERE条件后的执行顺序, </br>
         * 但我们仍应该将选择性高(过滤数据多)的条件放置在WHERE语句中的左边</br>
         * 
         * 故我们默认将精确查询放在左边，模糊查询放在右边
         */
        List<Object> params = new ArrayList<Object>();
        StringBuffer condition = new StringBuffer();

        String percizeSql = (String) percizeMap.get("sql");
        List<Object> percizeParams = (List<Object>) percizeMap.get("params");
        String fuzzySql = (String) fuzzyMap.get("sql");
        List<Object> fuzzyParams = (List<Object>) fuzzyMap.get("params");
        if ("ORACLE".equalsIgnoreCase(databaseType)) {
            if (StringUtils.isNotBlank(fuzzySql)) {
                condition.append(fuzzySql);
                params.addAll(fuzzyParams);
            }
            if (StringUtils.isNotBlank(percizeSql)) {
                if (condition.length() > 0) {
                    condition.append("AND ");
                }
                condition.append(percizeSql);
                params.addAll(percizeParams);
            }
        } else {
            if (StringUtils.isNotBlank(percizeSql)) {
                condition.append(percizeSql);
                params.addAll(percizeParams);
            }
            if (StringUtils.isNotBlank(fuzzySql)) {
                if (condition.length() > 0) {
                    condition.append("AND ");
                }
                condition.append(fuzzySql);
                params.addAll(fuzzyParams);
            }
        }

        if (condition.length() > 0) {
            StringBuffer keyWords = new StringBuffer(" WHERE ");
            condition = keyWords.append(condition);
        }

        result.put("sql", condition);
        result.put("params", params);

        return result;
    }

    /**
     * 构建具体查询字段，如果为空，就不作为查询条件
     * 
     * @param fieldList
     *            查询字段列表
     * @param jsonObject
     *            入参参数
     * @param isPercize
     *            是否精确查询
     * @return
     * @author Monk
     * @date 2020年8月17日 上午11:01:42
     */
    private Map<String, Object> buildCondition(List<Field> fieldList, JSONObject jsonObject, Boolean isPercize) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Object> params = new ArrayList<Object>();
        StringBuffer condition = new StringBuffer("");
        Boolean firstCondition = true;

        if (CollectionUtils.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                Object fieldValue = jsonObject.get(field.getFieldName());
                if (null != fieldValue) {
                    if (!firstCondition) {
                        condition.append("AND ");
                    }
                    if (isPercize) {
                        condition.append(field.getColumnName()).append(" = ? ");
                        params.add(String.valueOf(fieldValue));
                    } else {
                        condition.append(field.getColumnName()).append(" like ? ");
                        params.add("%" + fieldValue + "%");
                    }
                    firstCondition = false;
                }
            }
        }

        result.put("sql", condition.toString());
        result.put("params", params);

        return result;
    }

}
