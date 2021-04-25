/**
 * 
 * 文件名：EsbPointFieldLink.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.xml;

import java.io.Serializable;
import java.util.Date;

/**
 * 端到端分析字段配置项关系表
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年3月27日 下午4:48:58
 */
public class EsbPointFieldLink implements Serializable {
    
    public EsbPointFieldLink() {
        
    }
    
    public EsbPointFieldLink(String fieldNameEn, String sourceNameEn, String xPath) {
        this.fieldNameEn = fieldNameEn;
        this.sourceNameEn = sourceNameEn;
        this.xPath = xPath;
    }
    

    private static final long serialVersionUID = 8803312410416910850L;

    /** 关系ID */
    private Long linkId;

    /** 配置编码 */
    private String configCode;

    /** 字段英文名 */
    private String fieldNameEn;

    /** 源字段英文名 */
    private String sourceNameEn;

    /** 字段路径 */
    private String xPath;
    
    /** 数据字典类型 */
    private String fieldLookupType;

    /** 标识位 */
    private String enabledFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建时间 */
    private Date createdDate;

    /** 修改人 */
    private Long lastUpdateBy;

    /** 修改时间 */
    private Date lastUpdateDate;

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getFieldNameEn() {
        return fieldNameEn;
    }

    public void setFieldNameEn(String fieldNameEn) {
        this.fieldNameEn = fieldNameEn;
    }

    public String getSourceNameEn() {
        return sourceNameEn;
    }

    public void setSourceNameEn(String sourceNameEn) {
        this.sourceNameEn = sourceNameEn;
    }

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    public String getFieldLookupType() {
        return fieldLookupType;
    }

    public void setFieldLookupType(String fieldLookupType) {
        this.fieldLookupType = fieldLookupType;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return "EsbPointFieldLink [linkId=" + linkId + ", configCode=" + configCode + ", fieldNameEn="
                + fieldNameEn + ", sourceNameEn=" + sourceNameEn + ", xPath=" + xPath + ", fieldLookupType="
                + fieldLookupType + ", enabledFlag=" + enabledFlag + ", createdBy=" + createdBy + ", createdDate="
                + createdDate + ", lastUpdateBy=" + lastUpdateBy + ", lastUpdateDate=" + lastUpdateDate + "]";
    }

}
