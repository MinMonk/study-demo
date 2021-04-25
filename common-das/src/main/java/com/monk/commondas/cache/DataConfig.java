package com.monk.commondas.cache;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DataConfig {
    /**
     * 服务英文名
     */
    private String serviceNameEn;

    /**
     * 服务主版本
     */
    private String majorVersion;

    /**
     * 配置信息版本号
     */
    private String configVersion;

    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 数据源类型
     */
    private String dataSourceType;

    /**
     * 适配器操作类型
     */
    private String operationType;

    /**
     * 主表字段信息
     */
    private TableConfig master;

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

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public TableConfig getMaster() {
        return master;
    }

    public void setMaster(TableConfig master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}
