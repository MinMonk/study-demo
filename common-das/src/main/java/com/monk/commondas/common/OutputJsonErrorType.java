package com.monk.commondas.common;

public enum OutputJsonErrorType {
    /**
     * 成功
     */
    SUCCESS("S"),
    /**
     * 错误
     */
    ERROR("E"),
    /**
     * 警告
     */
    WARN("W"),
    /**
     * 信息
     */
    INFO("I"),
    /**
     * 中断
     */
    ABORTIVE("A");
    
    private OutputJsonErrorType(String value) {
        this.value = value;
    }
    
    private String value; 
    
    public String getValue() {
        return value;
    }

}
