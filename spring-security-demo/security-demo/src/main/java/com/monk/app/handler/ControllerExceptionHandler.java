package com.monk.app.handler;

import com.monk.app.exception.CustomUserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ControllerExceptionHandler
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomUserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerUserNotExistException(CustomUserNotExistException ex) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("userId", ex.getUserId());
        result.put("message", ex.getMessage());
        return result;
    }
}
