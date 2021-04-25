package com.monk.app.session;

import com.monk.app.validate.bean.ValidateCode;
import com.monk.app.validate.bean.ValidateCodeType;
import com.monk.app.validate.processor.ValidateCodeRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @ClassName SessionValidateCodeRepository
 * @Description: TODO
 * @Author Monk
 * @Date 2020/7/5
 * @Version V1.0
 **/
@Repository
public class SessionValidateCodeRepository implements ValidateCodeRepository {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private static final String SESSION_KEY_PREFIX = "SESSION_CODE_KEY_FOR_";

    @Override
    public void saveCode(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        sessionStrategy.setAttribute(request, getSessionKey(request, validateCodeType), validateCode);
    }

    /**
     * 功能描述: <br>
     * 〈根据请求的url获取存放到session中的key〉
     *
     * @Param: [request]
     * @Return: java.lang.String
     * @Author: Monk
     * @Date: 2020/4/12 12:16
     */
    private String getSessionKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        return SESSION_KEY_PREFIX + validateCodeType.toString().toUpperCase();
    }

    @Override
    public ValidateCode getCode(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String sessionKey = getSessionKey(request, validateCodeType);
        return (ValidateCode) sessionStrategy.getAttribute(request, sessionKey);
    }

    @Override
    public void removeCode(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String sessionKey = getSessionKey(request, validateCodeType);
        sessionStrategy.removeAttribute(request, sessionKey);
    }
}
