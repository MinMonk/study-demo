package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EsbService implements Serializable {

    private static final long serialVersionUID = -1809149722786979919L;

    private Long id;

    private String serviceNumber;

    private String serviceNameEn;

    private String serviceNameCN;

    private String standardVersion;

    private String majorVersion;

    private String minorVersion;

    private String runtimeVersion;

    private String description;

    private String serviceType;
    private String synOrAsync;

    private Long callBackSrvId;

    // 实现方式
    private String method;

    // 业务场景
    private String applicationSence;

    // 业务规则
    private String bussnessLogic;

    // 出错和异常处理机制
    private String exceptionHandle;

    // 数据量
    private String dataLength;

    // 响应时间要求
    private String timeResponse;

    // 服务安全和权限要求
    private String securityPermission;

    // 使用建议
    private String suggestion;

    // 服务状态
    private String serviceStatus;

    // WSDL 地址,服务封装地址
    private String wsdlUrl;

    // 提供方系统
    private Long providerApp;

    // 上线时间
    private Date onlineDate;

    /**
     * 规范文档名称
     */
    private String documentFileName;

    // 保留字段
    private String enabledFlag;

    private Long createdBy;

    private Long lastUpdateBy;

    private Date createdDate;

    private Date lastUpdateDate;

    private String paraValues;
    private String exceptionNlog;

    public String getExceptionNlog() {
        return exceptionNlog;
    }

    public void setExceptionNlog(String exceptionNlog) {
        this.exceptionNlog = exceptionNlog;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    public String getServiceNameCN() {
        return serviceNameCN;
    }

    public void setServiceNameCN(String serviceNameCN) {
        this.serviceNameCN = serviceNameCN;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public Long getProviderApp() {
        return providerApp;
    }

    public void setProviderApp(Long providerApp) {
        this.providerApp = providerApp;
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

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSynOrAsync() {
        return synOrAsync;
    }

    public void setSynOrAsync(String synOrAsync) {
        this.synOrAsync = synOrAsync;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApplicationSence() {
        return applicationSence;
    }

    public void setApplicationSence(String applicationSence) {
        this.applicationSence = applicationSence;
    }

    public String getBussnessLogic() {
        return bussnessLogic;
    }

    public void setBussnessLogic(String bussnessLogic) {
        this.bussnessLogic = bussnessLogic;
    }

    public String getExceptionHandle() {
        return exceptionHandle;
    }

    public void setExceptionHandle(String exceptionHandle) {
        this.exceptionHandle = exceptionHandle;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getTimeResponse() {
        return timeResponse;
    }

    public void setTimeResponse(String timeResponse) {
        this.timeResponse = timeResponse;
    }

    public String getSecurityPermission() {
        return securityPermission;
    }

    public void setSecurityPermission(String securityPermission) {
        this.securityPermission = securityPermission;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getParaValues() {
        return paraValues;
    }

    public void setParaValues(String paraValues) {
        this.paraValues = paraValues;
    }

    public Date getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(Date onlineDate) {
        this.onlineDate = onlineDate;
    }

    @Override
    public String toString() {
        return "EsbService [id=" + id + ", serviceNumber=" + serviceNumber + ", serviceNameEn=" + serviceNameEn
                + ", serviceNameCN=" + serviceNameCN + ", description=" + description + ", serviceType="
                + serviceType + ", synOrAsync=" + synOrAsync + ", method=" + method + ", applicationSence="
                + applicationSence + ", bussnessLogic=" + bussnessLogic + ", exceptionHandle=" + exceptionHandle
                + ", dataLength=" + dataLength + ", timeResponse=" + timeResponse + ", securityPermission="
                + securityPermission + ", suggestion=" + suggestion + ", serviceStatus=" + serviceStatus
                + ", wsdlUrl=" + wsdlUrl + ", providerApp=" + providerApp + ", enabledFlag=" + enabledFlag
                + ", createdBy=" + createdBy + ", lastUpdateBy=" + lastUpdateBy + ", createdDate=" + createdDate
                + ", lastUpdateDate=" + lastUpdateDate + "]";
    }

    public Long getCallBackSrvId() {
        return callBackSrvId;
    }

    public void setCallBackSrvId(Long callBackSrvId) {
        this.callBackSrvId = callBackSrvId;
    }

    public String getStandardVersion() {
        return standardVersion;
    }

    public void setStandardVersion(String standardVersion) {
        this.standardVersion = standardVersion;
    }

    public String getRuntimeVersion() {
        return runtimeVersion;
    }

    public void setRuntimeVersion(String runtimeVersion) {
        this.runtimeVersion = runtimeVersion;
    }

    /**
     * 验证版本号是否是指定格式的版本号<br />
     * <li>格式 大版本号.小版本号 <li>大版本号范围 1-10 <li>小版本号范围 0-99
     * 
     * @param version
     * @return true:是,false:不是
     */
    public static boolean validateStandardVersion(String version) {
        boolean flag = false;
        if (null != version && !"".equals(version)) {
            String pattern = "^(\\d{1})(.{1})(\\d{1,2})$";

            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);

            // 现在创建 matcher 对象
            Matcher m = r.matcher(version);
            if (m.find()) {
                flag = true;
            }
        }
        return flag;
    }

    public static String parseMajorVersion(String standardVersion) {
        String majorVersion = "";
        if (null != standardVersion && !"".equals(standardVersion)) {
            if (validateStandardVersion(standardVersion)) {
                majorVersion = standardVersion.split("[.]")[0];
            }
        }
        return majorVersion;
    }

    public static String parseMinorVersion(String standardVersion) {
        String minorVersion = "";
        if (null != standardVersion && !"".equals(standardVersion)) {
            if (validateStandardVersion(standardVersion)) {
                minorVersion = standardVersion.split("[.]")[1];
            }
        }
        return minorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public static void main(String[] args) {
        EsbService es = new EsbService();
        es.setStandardVersion("1.00");

        System.out.println("validate=" + validateStandardVersion(es.getStandardVersion()));
        System.out.println("大版本号=" + es.getMajorVersion());
        System.out.println("小版本号=" + es.getMinorVersion());
    }

}
