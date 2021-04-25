package com.example.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EsbServiceParameter implements Serializable {

    private static final long serialVersionUID = -2789031212042047663L;

    private Long id;

    private Long serviceId;

    private Long parameterParentId;

    private Long parameterSequence;

    private String parameterNameEn;

    private String parameterNameCh;

    private String parameterType;

    private String dataType;

    // Y不可为空,N相反
    private String isNullFlag;

    private String routeFlag;

    private Long dataLength;

    private String constraints;

    private String remark;

    // 保留字段
    private String enabledFlag;

    private Long createdBy;

    private Long lastUpdateBy;

    private Date createdDate;

    private Date lastUpdateDate;

    private String idStr;

    private String paraParentIdStr;
    private String example;
    private String defaultValue;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    private List<EsbServiceParameter> subParmList = new ArrayList<EsbServiceParameter>();

    public EsbServiceParameter() {

    }

    public EsbServiceParameter(Long id, Long serviceId, Long parameterParentId, Long parameterSequence,
            String parameterNameEn, String parameterNameCh, String parameterType, String dataType,
            String isNullFlag, String routeFlag, Long dataLength, String constraints, String remark,
            String enabledFlag, Long createdBy, Long lastUpdateBy, Date createdDate, Date lastUpdateDate) {
        super();
        this.id = id;
        this.serviceId = serviceId;
        this.parameterParentId = parameterParentId;
        this.parameterSequence = parameterSequence;
        this.parameterNameEn = parameterNameEn;
        this.parameterNameCh = parameterNameCh;
        this.parameterType = parameterType;
        this.dataType = dataType;
        this.isNullFlag = isNullFlag;
        this.routeFlag = routeFlag;
        this.dataLength = dataLength;
        this.constraints = constraints;
        this.remark = remark;
        this.enabledFlag = enabledFlag;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.createdDate = createdDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getDataLength() {
        return dataLength;
    }

    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIsNullFlag() {
        return isNullFlag;
    }

    public void setIsNullFlag(String isNullFlag) {
        this.isNullFlag = isNullFlag;
    }

    public Long getParameterParentId() {
        return parameterParentId;
    }

    public void setParameterParentId(Long parameterParentId) {
        this.parameterParentId = parameterParentId;
    }

    public Long getParameterSequence() {
        return parameterSequence;
    }

    public void setParameterSequence(Long parameterSequence) {
        this.parameterSequence = parameterSequence;
    }

    public String getParameterNameEn() {
        return parameterNameEn;
    }

    public void setParameterNameEn(String parameterNameEn) {
        this.parameterNameEn = parameterNameEn;
    }

    public String getParameterNameCh() {
        return parameterNameCh;
    }

    public void setParameterNameCh(String parameterNameCh) {
        this.parameterNameCh = parameterNameCh;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
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

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getParaParentIdStr() {
        return paraParentIdStr;
    }

    public void setParaParentIdStr(String paraParentIdStr) {
        this.paraParentIdStr = paraParentIdStr;
    }

    public String getRouteFlag() {
        return routeFlag;
    }

    public void setRouteFlag(String routeFlag) {
        this.routeFlag = routeFlag;
    }

    public List<EsbServiceParameter> getSubParmList() {
        return subParmList;
    }

    public void setSubParmList(List<EsbServiceParameter> subParmList) {
        this.subParmList = subParmList;
    }

}
