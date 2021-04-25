package com.monk.app.validate.generator;

import com.monk.app.validate.bean.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author Monk
 */
public interface CodeGenerator {

    /**
     * 功能描述: <br>
     * 〈生成验证码〉
     * @Param: [request]
     * @Return: com.monk.security.validate.bean.ValidateCode
     * @Author: Monk
     * @Date: 2020/4/12 15:01
     */
    ValidateCode generateCode(ServletWebRequest request);
}
