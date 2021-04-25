package com.monk.common.jaxb.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Field {
    @XmlElement(name = "FIELD_NAME", required = true, nillable = false)
    private String fieldName;

    @XmlElement(name = "COLUMN_NAME", required = true, nillable = false)
    private String columnName;

    @XmlElement(name = "COLUMN_TYPE", required = true, nillable = false)
    private String columnType;

    @XmlElement(name = "DEFAULT_VALUE", required = false, nillable = true)
    private String defaultValue;

    @XmlElement(name = "IS_PRECISE", required = false, nillable = true)
    private Boolean isPrecise;

    @XmlElement(name = "IS_FUZZY", required = false, nillable = true)
    private Boolean isFuzzy;

    @XmlElement(name = "IS_RESULT", required = false, nillable = true)
    private Boolean isResult;

    @XmlElement(name = "IS_IMPORT", required = false, nillable = true)
    private Boolean isImport;

    @XmlElement(name = "IS_DELETE", required = false, nillable = true)
    private Boolean isDelete;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getPrecise() {
        return isPrecise;
    }

    public void setPrecise(Boolean precise) {
        isPrecise = precise;
    }

    public Boolean getFuzzy() {
        return isFuzzy;
    }

    public void setFuzzy(Boolean fuzzy) {
        isFuzzy = fuzzy;
    }

    public Boolean getResult() {
        return isResult;
    }

    public void setResult(Boolean result) {
        isResult = result;
    }

    public Boolean getImport() {
        return isImport;
    }

    public void setImport(Boolean anImport) {
        isImport = anImport;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
