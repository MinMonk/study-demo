package com.monk.app.validate.processor.impl;

import com.monk.app.constant.SecurityConstant;
import com.monk.app.exception.CustomValidateCodeException;
import com.monk.app.validate.processor.ValidateCodeProcessor;
import com.monk.app.validate.generator.CodeGenerator;
import com.monk.app.validate.bean.ValidateCode;
import com.monk.app.validate.bean.ValidateCodeType;
import com.monk.app.validate.processor.ValidateCodeRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @ClassName ValidateCodeProcessorImpl
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/12
 * @Version V1.0
 **/
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有的 {@link CodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, CodeGenerator> validateCodeGenerators;

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    @Override
    public void createCode(ServletWebRequest request) throws Exception {
        C validateCode = generateCode(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 功能描述: <br>
     * 〈根据url中的参数获取校验码的类型〉
     *
     * @Param: [request]
     * @Return: com.monk.security.validate.bean.ValidateCodeType
     * @Author: Monk
     * @Date: 2020/4/12 11:56
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }


    /**
     * 功能描述: <br>
     * 〈生成验证码〉
     *
     * @Param: [request]
     * @Return: C
     * @Author: Monk
     * @Date: 2020/4/12 12:15
     */
    private C generateCode(ServletWebRequest request) {
        String type = getValidateCodeType(request).toString().toLowerCase();
        String generateName = type + CodeGenerator.class.getSimpleName();
        CodeGenerator codeGenerator = validateCodeGenerators.get(generateName);
        if (null == codeGenerator) {
            throw new CustomValidateCodeException("验证码生成器" + generateName + "不存在");
        }
        return (C) codeGenerator.generateCode(request);
    }

    /**
     * 功能描述: <br>
     * 〈发送验证码，由子类实现〉
     *
     * @Param: [request, validateCode]
     * @Return: void
     * @Author: Monk
     * @Date: 2020/4/12 12:13
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 功能描述: <br>
     * 〈保存验证码到session中〉
     *
     * @Param: [request, validateCode]
     * @Return: void
     * @Author: Monk
     * @Date: 2020/4/12 12:15
     */
    private void save(ServletWebRequest request, C validateCode) {
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        //sessionStrategy.setAttribute(request, getSessionKey(request), code);
        validateCodeRepository.saveCode(request, code, getValidateCodeType(request));
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
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    @Override
    public void validateCode(ServletWebRequest request) {
        ValidateCodeType validateCodeType = getValidateCodeType(request);

        ValidateCode codeInSession = validateCodeRepository.getCode(request, validateCodeType);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    validateCodeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new CustomValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new CustomValidateCodeException("验证码不能为空");
        }
        if (null == codeInSession) {
            throw new CustomValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, SecurityConstant.SESSION_SMS_CODE_KEY);
            throw new CustomValidateCodeException("验证码已经过期");
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new CustomValidateCodeException("验证码不匹配");
        }
        //sessionStrategy.removeAttribute(request, SecurityConstant.SESSION_SMS_CODE_KEY);
        validateCodeRepository.removeCode(request, validateCodeType);
    }
}
