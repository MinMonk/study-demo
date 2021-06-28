package com.monk.common.jaxb.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Table {
    @XmlElement(name = "TABLE_NAME", required = true, nillable = false)
    private String tableName;

    @XmlElement(name = "ENTITY_NAME", required = true, nillable = false)
    private String entityName;

    @XmlElement(name = "TABLE_TYPE", required = true, nillable = false)
    private String tableType;

    @XmlElementWrapper(name = "FIELDS", nillable = false)
    @XmlElement(name = "FIELD")
    private List<Field> fields;

    @XmlElementWrapper(name = "SLAVE_TABLES", nillable = true)
    @XmlElement(name = "TABLE")
    private List<Table> slaveTables;

    @XmlElementWrapper(name = "KEYS", nillable = true)
    @XmlElement(name = "KEY")
    private List<Key> keys;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Table> getSlaveTables() {
        return slaveTables;
    }

    public void setSlaveTables(List<Table> slaveTables) {
        this.slaveTables = slaveTables;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
