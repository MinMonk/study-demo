package com.monk.app.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * @ClassName ImageCodeException
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
public class CustomValidateCodeException extends AuthenticationException {

    public CustomValidateCodeException(String msg){
        super(msg);
    }
}
