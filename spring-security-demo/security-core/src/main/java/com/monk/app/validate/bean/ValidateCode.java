package com.monk.app.validate.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName Code
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/12
 * @Version V1.0
 **/
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = -2893283545069157139L;

    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, long expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
