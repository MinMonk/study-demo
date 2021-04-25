package com.monk.commondas.cache;

import com.monk.commondas.bean.Field;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableConfig {
    /**
     * 实体名称
     */
    private String entityName;
    
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表类型[TABLE|VIEW]
     */
    private String tableType;
    
    /**
     * 基础SQL(where关键字前的sql)
     */
    private String basicSql;

    /**
     * 外键字段
     */
    private List<Map<String, String>> foreignKeys = new ArrayList<Map<String, String>>();
    
    /**
     * 主键字段
     */
    private List<Field> primaryKeys = new ArrayList<Field>();
    
    /**
     * 查询结果集字段
     */
    private List<Field> resultList = new ArrayList<Field>();
    
    /**
     * 插入字段
     */
    private List<Field> importList = new ArrayList<Field>();
    
    /**
     * 精确查询字段
     */
    private List<Field> percizeList = new ArrayList<Field>();
    
    /**
     * 模糊查询字段
     */
    private List<Field> fuzzyList = new ArrayList<Field>();
    
    private List<Field> fieldList = new ArrayList<Field>();
    
    /**
     * 从表字段信息
     */
    private List<TableConfig> slaves;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getBasicSql() {
        return basicSql;
    }

    public void setBasicSql(String basicSql) {
        this.basicSql = basicSql;
    }

    public List<Map<String, String>> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<Map<String, String>> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public List<Field> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<Field> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<Field> getResultList() {
        return resultList;
    }

    public void setResultList(List<Field> resultList) {
        this.resultList = resultList;
    }

    public List<Field> getImportList() {
        return importList;
    }

    public void setImportList(List<Field> importList) {
        this.importList = importList;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Field> getPercizeList() {
        return percizeList;
    }

    public void setPercizeList(List<Field> percizeList) {
        this.percizeList = percizeList;
    }

    public List<Field> getFuzzyList() {
        return fuzzyList;
    }

    public void setFuzzyList(List<Field> fuzzyList) {
        this.fuzzyList = fuzzyList;
    }

    public List<TableConfig> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<TableConfig> slaves) {
        this.slaves = slaves;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
