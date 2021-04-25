package com.monk.commondas.exception;

public class ValidateException extends RuntimeException{

    /**
     * 
     */
    private static final long serialVersionUID = 7804210938451181401L;
    
    /** 返回码 */
    private String code;

    /** 返回错信息 */
    private String message;
    
    public ValidateException() {
        
    }
    
    public ValidateException(String message) {
        super(message);
        this.message = message;
    }
    
    public ValidateException(String code, String message) {
        this(message);
        this.code = code;
    }
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidateException [code=" + code + ", message=" + message + "]";
    }
}
