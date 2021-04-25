/**
 * 
 * 文件名：EsbPointField.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.xml;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 端到端分析字段配置项表
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年3月27日 下午4:48:39
 */
public class EsbPointField implements Serializable {
    
    public EsbPointField() {}
    public EsbPointField(String fieldNameEn, int fieldLevel, String fieldType) {
        this.fieldNameEn = fieldNameEn;
        this.fieldLevel = fieldLevel;
        this.fieldType = fieldType;
    }

    private static final long serialVersionUID = -1078843423091799260L;

    @JsonIgnore
    private Long fieldId;

    /** 端对端编码 */
    @JsonIgnore
    private String pointCode;

    /** 字段类型 */
    private String fieldType;

    /** 字段英文名 */
    private String fieldNameEn;

    /** 字段中文名 */
    private String fieldNameCn;

    /** 字段位置 */
    private String fieldPosition;

    /** 字段展示层级 */
    private Integer fieldLevel;

    /** 字段顺序 */
    @JsonIgnore
    private Long fieldOrder;

    /** 字段组 */
    private String fieldGroup;
    
    /** 字段是否是关键字查询条件 */
    private String isKeyword;

    /** 默认值 */
    private String defaultValue;

    /** 标识位 */
    @JsonIgnore
    private String enabledFlag;

    /** 创建人 */
    @JsonIgnore
    private Long createdBy;

    /** 创建时间 */
    @JsonIgnore
    private Date createdDate;

    /** 修改人 */
    @JsonIgnore
    private Long lastUpdateBy;

    /** 修改时间 */
    @JsonIgnore
    private Date lastUpdateDate;

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldNameEn() {
        return fieldNameEn;
    }

    public void setFieldNameEn(String fieldNameEn) {
        this.fieldNameEn = fieldNameEn;
    }

    public String getFieldNameCn() {
        return fieldNameCn;
    }

    public void setFieldNameCn(String fieldNameCn) {
        this.fieldNameCn = fieldNameCn;
    }

    public String getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(String fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public Integer getFieldLevel() {
        return fieldLevel;
    }

    public void setFieldLevel(Integer fieldLevel) {
        this.fieldLevel = fieldLevel;
    }

    public Long getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Long fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFieldGroup() {
        return fieldGroup;
    }

    public void setFieldGroup(String fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    public String getIsKeyword() {
        return isKeyword;
    }

    public void setIsKeyword(String isKeyword) {
        this.isKeyword = isKeyword;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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
        return "EsbPointField [fieldId=" + fieldId + ", pointCode=" + pointCode + ", fieldType=" + fieldType
                + ", fieldNameEn=" + fieldNameEn + ", fieldNameCn=" + fieldNameCn + ", fieldPosition="
                + fieldPosition + ", fieldLevel=" + fieldLevel + ", fieldOrder=" + fieldOrder + ", fieldGroup="
                + fieldGroup + ", isKeyword=" + isKeyword + ", defaultValue=" + defaultValue + ", enabledFlag="
                + enabledFlag + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastUpdateBy="
                + lastUpdateBy + ", lastUpdateDate=" + lastUpdateDate + "]";
    }

}
