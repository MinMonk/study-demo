package com.monk.common.jaxb.cache;

import com.monk.common.jaxb.bean.Field;
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

    private String tableType;

    /**
     * 外键字段
     */
    private List<Map<String, String>> mappingList;
    private List<Field> resultList = new ArrayList<Field>();
    private List<Field> importList = new ArrayList<Field>();
    private List<Field> deleteList = new ArrayList<Field>();
    private List<Field> percizeList = new ArrayList<Field>();
    private List<Field> fuzzyList = new ArrayList<Field>();

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

    public List<Map<String, String>> getMappingList() {
        return mappingList;
    }

    public void setMappingList(List<Map<String, String>> mappingList) {
        this.mappingList = mappingList;
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

    public List<Field> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<Field> deleteList) {
        this.deleteList = deleteList;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
