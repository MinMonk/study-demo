package com.monk.commondas.common;

public enum BIZErrorCode {

    /**
     * 服务请求输入完整性校验不通过
     */
    VALIDATE_ERROR("BIZ-01", "服务请求输入完整性校验不通过"),
    /**
     * 服务业务规则处理不通过
     */
    BIZ_ERROR("BIZ-02", "服务业务规则处理不通过"),
    /**
     * 业务系统处理超时
     */
    TIMEOUT("BIZ-03", "业务系统处理超时"),
    /**
     * 业务系统其它处理异常
     */
    OTHER_ERROR("BIZ-04", "业务系统其它处理异常");

    private String code;
    private String msg;

    private BIZErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
    
    public String getMsg() {
        return msg;
    }
}
