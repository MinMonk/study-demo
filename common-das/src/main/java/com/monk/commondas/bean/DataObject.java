package com.monk.commondas.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DATA_OBJECT", namespace = "http://monk.com/CommonDAS")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataObject {
    @XmlElement(name = "DATA_OBJECT_NAME", required = true, nillable = false)
    private String dataObjectName;

    @XmlElement(name = "DATA_SOURCE_NAME", required = true, nillable = false)
    private String dataSourceName;

    @XmlElement(name = "OPERATION_TYPE", required = true, nillable = false)
    private String operationType;

    @XmlElement(name = "MASTER", required = true, nillable = false)
    private Table master;

    @XmlElement(name = "DATA_SOURCE_TYPE", required = false, nillable = true)
    private String dataSourceType;

    @XmlElement(name = "IS_PAGE", required = false, nillable = true)
    private String isPage;

    public String getDataObjectName() {
        return dataObjectName;
    }

    public void setDataObjectName(String dataObjectName) {
        this.dataObjectName = dataObjectName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Table getMaster() {
        return master;
    }

    public void setMaster(Table master) {
        this.master = master;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getIsPage() {
        return isPage;
    }

    public void setIsPage(String isPage) {
        this.isPage = isPage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
