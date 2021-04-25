package com.monk.commondas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.monk.commondas.constant.DasConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.monk.commondas.bean.Field;
import com.monk.commondas.cache.TableConfig;
import com.monk.commondas.exception.FieldIllegalException;
import com.monk.commondas.exception.FieldRequirelException;

public class DasUtil {

    private static final Logger logger = LoggerFactory.getLogger(DasUtil.class);

    /**
     * 获取关联字段
     * 
     * @param config
     *            表配置信息
     * @param parentConfig
     *            父表配置信息
     * @return
     * @author Monk
     * @date 2020年8月14日 下午4:45:32
     */
    public static List<Field> getForeignKeys(TableConfig config, TableConfig parentConfig) {
        List<Map<String, String>> foreignKeyList = config.getForeignKeys();
        if (CollectionUtils.isEmpty(foreignKeyList) || null == parentConfig) {
            return new ArrayList<Field>(0);
        }

        List<Field> primaryKeyList = config.getPrimaryKeys();

        Set<String> primaryKeys = new HashSet<String>();
        for (Field field : primaryKeyList) {
            primaryKeys.add(field.getColumnName());
        }

        // 剔除已经校验过的主键字段
        Set<String> foreignKeys = new HashSet<String>();
        for (Map<String, String> map : foreignKeyList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!primaryKeys.contains(entry.getValue())) {
                    foreignKeys.add(entry.getValue());
                }
            }
        }

        List<Field> uniqueFieldList = new ArrayList<Field>();
        for (Field field : parentConfig.getFieldList()) {
            if (foreignKeys.contains(field.getColumnName())) {
                uniqueFieldList.add(field);
            }
        }

        return uniqueFieldList;
    }

    /**
     * 根据表列明获取表的Field对象信息
     * 
     * @param columnName
     *            表列名
     * @param fieldList
     *            表的全部字段信息集合
     * @return Field对象信息
     * @author Monk
     * @date 2020年8月15日 上午11:53:58
     */
    public static Field getFieldByColumnName(String columnName, List<Field> fieldList) {
        for (Field field : fieldList) {
            if (field.getColumnName().equals(columnName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 构建where条件
     * 
     * @param config
     *            当前表配置信息
     * @param parentConfig
     *            父表配置信息
     * @param jsonObject
     *            当前表入参json对象
     * @param parentObject
     *            父表入参json对象
     * @param needForeign
     *            是否需要拼接关联关系字段
     * @return
     * @author Monk
     * @date 2020年8月15日 上午11:50:22
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> buildWhereCondition(TableConfig config, TableConfig parentConfig,
            JSONObject jsonObject, JSONObject parentObject, Boolean needForeign) {
        Map<String, Object> result = new HashMap<String, Object>();
        StringBuffer whereSql = new StringBuffer(" WHERE ");
        List<Object> whereParams = new ArrayList<Object>();

        // 构建主键where条件
        Map<String, Object> primaryConditionMap = buildCondition(config.getPrimaryKeys(), jsonObject);
        String primarySql = MapUtils.getString(primaryConditionMap, "sql");
        List<Object> primaryParams = (List<Object>) MapUtils.getObject(primaryConditionMap, "params");
        primarySql = primarySql.substring(4);
        whereSql.append(primarySql);
        whereParams.addAll(primaryParams);

        //if ("update".equals(type) || "delete".equals(type)) {
        if (needForeign) {
            // 构建关联关系where条件
            // 只有update or delete时，where条件才需要跟上关联关系
            List<Field> foreignKeys = DasUtil.getForeignKeys(config, parentConfig);
            Map<String, Object> foreignConditionMap = buildCondition(foreignKeys, parentObject);
            String foreignSql = MapUtils.getString(foreignConditionMap, "sql");
            List<Object> foreignParams = (List<Object>) MapUtils.getObject(foreignConditionMap, "params");
            whereSql.append(foreignSql);
            whereParams.addAll(foreignParams);
        }

        result.put("sql", whereSql);
        result.put("params", whereParams);
        return result;
    }

    /**
     * 拼接where条件
     * 
     * @param fieldList
     *            查询条件
     * @param jsonObject
     *            入参值
     * @return where条件
     * @author Monk
     * @date 2020年8月15日 上午11:50:02
     */
    private static Map<String, Object> buildCondition(List<Field> fieldList, JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        for (Field field : fieldList) {
            sql.append("AND ").append(field.getColumnName()).append(" = ? ");
            params.add(jsonObject.get(field.getFieldName()));
        }
        result.put("sql", sql);
        result.put("params", params);
        return result;
    }

    /**
     * 校验具体字段值
     * 
     * @param entityName
     *            实体名称
     * @param field
     *            字段对象
     * @param value
     *            入参object
     * @author Monk
     * @date 2020年8月22日 上午11:46:13
     */
    public static void validateFieldValue(String entityName, Field field, Object value) {
        String fieldName = field.getFieldName();
        String columnType = field.getColumnType();
        if (null == value) {
            String msg = String.format("The input_json properties [%s.%s] is require.", entityName, fieldName);
            throw new FieldRequirelException(msg);
        }

        String fieldValueStr = String.valueOf(value);
        if (columnType.toUpperCase().contains("NUMBER")) {
            try {
                Long.valueOf(fieldValueStr);
            } catch (NumberFormatException e) {
                logger.error("{}", e);
                String msg = String.format(
                        "The value of the field property [%s.%s] is not a legitimate number. the value is [%s]",
                        new Object[] { entityName, fieldName, value });
                throw new FieldIllegalException(msg);
            }
        }

        if (columnType.toUpperCase().contains("CHAR")) {
            Long length = field.getFieldLength();
            int fieldValueLength = fieldValueStr.length();
            if (fieldValueLength > length.intValue()) {
                String msg = String.format(
                        "The value of the field property [%s.%s] is too large (actual value : %s, maximum : %s). the value is [%s]",
                        new Object[] { entityName, fieldName, fieldValueLength, length, value });
                throw new FieldIllegalException(msg);
            }
        }

        if (columnType.toUpperCase().contains("DATE")) {
            String fieldConstraint = field.getFieldConstraint();
            if (StringUtils.isBlank(fieldConstraint) || fieldConstraint.equals(DasConstant.DEFAULT_FIELD_CONSTRAINT)) {
                fieldValueStr = fieldValueStr.replaceAll("[- :]", "");
                field.setFieldConstraint(DasConstant.DEFAULT_FIELD_CONSTRAINT);
                
                if(fieldValueStr.length() < 14) {
                    fieldValueStr = String.format("%-14s", fieldValueStr).replace(' ', '0');
                }
                
            }
            SimpleDateFormat sdf = new SimpleDateFormat(field.getFieldConstraint());
            sdf.setLenient(false);
            try {
                sdf.parse(fieldValueStr);
            } catch (ParseException e) {
                logger.error("{}", e);
                String msg = String.format(
                        "The value of the field property [%s.%s] is illegal, convert to date faield. the value is [%s]",
                        new Object[] { entityName, fieldName, value });
                throw new FieldIllegalException(msg);
            }
        }

        if (columnType.toUpperCase().contains("TIMESTAMP")) {
            try {
                Long fieldValue = Long.valueOf(fieldValueStr);
                Date date = new Date(fieldValue * 1000);
                SimpleDateFormat sdf =  new SimpleDateFormat();
                sdf.setLenient(false);
                String dateStr = sdf.format(date);
                sdf.parse(dateStr);
            } catch (Exception e) {
                logger.error("{}", e);
                String msg = String.format(
                        "The value of the field property [%s.%s] is illegal, convert to timestamp faield. the value is [%s]",
                        new Object[] { entityName, fieldName, value });
                throw new FieldIllegalException(msg);
            }
        }
    }
    
    /**
     * 对FieldList去重
     * @param fieldList  列集合
     * @return  去重后的列
     * @author Monk
     * @date 2020年8月27日 下午6:14:23
     */
    public static List<Field> removeDuplicateField(List<Field> fieldList){
        Set<String> columnNameSet = new HashSet<String>();
        List<Field> result = new ArrayList<Field>();
        if (CollectionUtils.isNotEmpty(fieldList)) {
            for(Field field : fieldList) {
                String columnName = field.getColumnName();
                if(!columnNameSet.contains(columnName)) {
                    result.add(field);
                }
                columnNameSet.add(columnName);
            }
        }
        return result;
    }
}
