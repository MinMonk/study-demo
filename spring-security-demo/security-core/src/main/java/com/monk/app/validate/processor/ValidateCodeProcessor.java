package com.monk.app.validate.processor;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @ClassName ValidateCodeProcessor
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/12
 * @Version V1.0
 **/
public interface ValidateCodeProcessor {

    String SESSION_KEY_PREFIX = "SESSION_CODE_KEY_FOR_";

    /**
     * 功能描述: <br>
     * 〈创建验证码〉
     * @Param: [request]
     * @Return: void
     * @Author: Monk
     * @Date: 2020/4/12 12:29
     */
    void createCode(ServletWebRequest request) throws Exception;

    /*
     * 功能描述: <br>
     * 〈校验验证码〉
     * @Param: [request]
     * @Return: void
     * @Author: Monk
     * @Date: 2020/5/3 1:52
     */
    void validateCode(ServletWebRequest request);
}
