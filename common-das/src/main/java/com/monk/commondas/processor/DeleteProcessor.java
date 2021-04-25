package com.monk.commondas.processor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.monk.commondas.cache.DataConfig;
import com.monk.commondas.cache.TableConfig;
import com.monk.commondas.exception.FieldRequirelException;
import com.monk.commondas.exception.ValidateException;
import com.monk.commondas.util.DasUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.monk.commondas.bean.Field;
import com.monk.commondas.constant.DbAdapterServiceType;
import com.monk.commondas.util.DasJdbcUtil;

public class DeleteProcessor extends AbstractDASProcessor {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DeleteProcessor.class);

    /**
     * 基础删除SQL模板
     */
    private static String DELETE_SQL_FORMAT = "DELETE FROM %s";

    public DeleteProcessor(String configStr, String configVersion, String serviceNameEn, String majorVersion) {
        super(configStr, configVersion, serviceNameEn, majorVersion);
        super.operationType = DbAdapterServiceType.DELETE;
    }

    @Override
    protected void buildBasicSql(TableConfig tableConfig) {
        String basicSql = String.format(DELETE_SQL_FORMAT, tableConfig.getTableName());

        if (logger.isDebugEnabled()) {
            logger.debug("delete basic sql:{}", basicSql);
        }
        tableConfig.setBasicSql(basicSql);
    }

    /**
     * 校验逻辑：<br/>
     * 删除时，主表主键字段和从表关联字段不能为空
     */
    @Override
    protected void validateInputJson(DataConfig dataConfig, JSONObject jsonObject) {
        TableConfig master = dataConfig.getMaster();

        List<Field> validateFieldList = new ArrayList<Field>(0);
        List<Field> primaryFieldList = master.getPrimaryKeys();
        List<Field> foreignFieldList = getForeignFields(master);
        validateFieldList.addAll(primaryFieldList);
        validateFieldList.addAll(foreignFieldList);
        if (CollectionUtils.isNotEmpty(validateFieldList)) {
            Object masterValue = jsonObject.get(master.getEntityName());
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
                DasUtil.validateFieldValue(table.getEntityName(), field, value);
            }
        } else {
            Object value = ((JSONObject) object).get(fieldName);
            DasUtil.validateFieldValue(table.getEntityName(), field, value);
        }
    }

    /**
     * 获取关联字段集合
     * 
     * @param master
     *            主表配置信息
     * @return 关联字段集合
     * @author Monk
     * @date 2020年8月22日 下午2:29:10
     */
    private List<Field> getForeignFields(TableConfig master) {
        List<Field> primaryFieldList = master.getPrimaryKeys();
        Set<String> primaryKeys = new HashSet<String>();
        for (Field field : primaryFieldList) {
            primaryKeys.add(field.getColumnName());
        }

        Set<String> foreignKeys = new HashSet<String>();
        List<TableConfig> slaveTableList = master.getSlaves();
        if (CollectionUtils.isNotEmpty(slaveTableList)) {
            for (TableConfig slaveTable : slaveTableList) {
                List<Map<String, String>> foreignKeyList = slaveTable.getForeignKeys();
                for (Map<String, String> map : foreignKeyList) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (!primaryKeys.contains(entry.getKey())) {
                            foreignKeys.add(entry.getKey());
                        }
                    }
                }
            }
        }

        List<Field> foreignFieldList = new ArrayList<Field>();
        for (Field field : master.getFieldList()) {
            if (foreignKeys.contains(field.getColumnName())) {
                foreignFieldList.add(field);
            }
        }
        return foreignFieldList;
    }

    @Override
    public Map<String, Object> executeSql(DataConfig dataConfig, JSONObject jsonObject) {
        // 先执行主表
        TableConfig master = dataConfig.getMaster();
        Object masterValue = jsonObject.get(master.getEntityName());

        processSingleTable(master, null, masterValue, null);

        return new HashMap<String, Object>(0);
    }

    /**
     * 处理单张表
     * 
     * @param currTable
     *            当前表配置信息
     * @param parentTable
     *            父表配置信息
     * @param currObject
     *            当前表入参对象
     * @param parentObject
     *            父表入参对象
     * @author Monk
     * @date 2020年8月15日 下午1:52:22
     */
    private void processSingleTable(TableConfig currTable, TableConfig parentTable, Object currObject,
            JSONObject parentObject) {
        if (currObject instanceof JSONArray) {
            JSONArray jsonArray = ((JSONArray) currObject);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject value = jsonArray.getJSONObject(i);

                // 先执行从表(如果主从表之间存在主外键关系，需要先删除从表的数据库才可以再删除主表的数据)
                for (TableConfig slave : currTable.getSlaves()) {
                    deleteSlaveTable(slave, currTable, value);
                }

                // 再执行主表
                processSingleObject(currTable, parentTable, value, parentObject);
            }
        } else if (currObject instanceof JSONObject) {
            JSONObject value = (JSONObject) currObject;

            // 先执行从表
            for (TableConfig slave : currTable.getSlaves()) {
                deleteSlaveTable(slave, currTable, value);
            }

            // 再执行主表
            processSingleObject(currTable, parentTable, value, parentObject);
        } else {
            logger.warn("unknown type for object. {}", currObject);
        }
    }

    private void deleteSlaveTable(TableConfig currTable, TableConfig parentTable, JSONObject parentValue) {
        List<Map<String, String>> foreignKeyList = currTable.getForeignKeys();
        if (CollectionUtils.isEmpty(foreignKeyList)) {
            throw new ValidateException("主从关联关系不允许为空，请维护主从关联关系");
        }
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        sql.append(currTable.getBasicSql());
        sql.append(" WHERE ");
        Boolean firstFlag = true;
        for (Map<String, String> foreignKey : foreignKeyList) {
            if (!firstFlag) {
                sql.append(" AND ");
            }
            for (Map.Entry<String, String> entry : foreignKey.entrySet()) {
                String parentTableColumn = entry.getKey();
                String currTableColumm = entry.getValue();
                Field parentField = DasUtil.getFieldByColumnName(parentTableColumn, parentTable.getFieldList());
                sql.append(currTableColumm).append(" = ? ");
                Object value = parentValue.get(parentField.getFieldName());
                if (null == value) {
                    String msg = String.format(
                            "Failed to delete slave table [%s] data. because the relation key [%s] is null.",
                            currTable.getTableName(), currTableColumm);
                    logger.error("{}", msg);
                    throw new FieldRequirelException(msg);
                }
                params.add(value);
            }
            firstFlag = false;
        }

        logger.info("delete sql:[{}], params:{}", sql.toString(), params.toArray());
        try {
            DasJdbcUtil.executeBatchNativeSql(conn, sql.toString(), params.toArray());
        } catch (SQLException e) {
            logger.error("execute sql failed. The delete sql:[{}], params:{}", sql.toString(), params.toArray());
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理当对象
     * 
     * @param currObject
     *            当前表配置信息
     * @param parentTable
     *            父表配置信息
     * @param currValue
     *            当前表入参对象
     * @param parentValue
     *            父表入参对象
     * @author Monk
     * @date 2020年8月15日 下午1:52:55
     */
    @SuppressWarnings("unchecked")
    private void processSingleObject(TableConfig currObject, TableConfig parentTable, JSONObject currValue,
            JSONObject parentValue) {
        String basicSql = currObject.getBasicSql();
        Map<String, Object> slaveMap = DasUtil.buildWhereCondition(currObject, parentTable, currValue, parentValue,
                true);
        String slaveConditionSQL = MapUtils.getString(slaveMap, "sql");
        List<Object> slaveParams = (List<Object>) MapUtils.getObject(slaveMap, "params");
        basicSql = basicSql + slaveConditionSQL;
        logger.info("delete sql:[{}], params:{}", basicSql, slaveParams.toArray());
        try {
            DasJdbcUtil.executeBatchNativeSql(conn, basicSql, slaveParams.toArray());
        } catch (SQLException e) {
            logger.error("execute sql failed. The delete sql:[{}], params:{}", basicSql, slaveParams.toArray());
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
    }

}
