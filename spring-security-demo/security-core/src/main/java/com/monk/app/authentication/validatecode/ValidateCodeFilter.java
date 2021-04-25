package com.monk.app.authentication.validatecode;

import com.monk.app.constant.SecurityConstant;
import com.monk.app.exception.CustomValidateCodeException;
import com.monk.app.propertites.SecurityProperties;
import com.monk.app.validate.ValidateCodeProcessorHolder;
import com.monk.app.validate.bean.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ValidateCodeFilter
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(ValidateCodeFilter.class);

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailedHandler;

    private Map<String, ValidateCodeType> urlMaps = new HashMap<String, ValidateCodeType>();

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        addUrlToMap(securityProperties.getValidateCode().getImageCode().getUrls(), ValidateCodeType.IMAGE);
        urlMaps.put(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);

        addUrlToMap(securityProperties.getValidateCode().getSmsCode().getUrls(), ValidateCodeType.SMS);
        urlMaps.put(SecurityConstant.DEFAULT_MOBILE_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.SMS);
    }

    private void addUrlToMap(String urlStr, ValidateCodeType validateCodeType) {
        if (StringUtils.isNotBlank(urlStr)) {
            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlStr, ",");
            for (String url : configUrls) {
                urlMaps.put(url, validateCodeType);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType validateCodeType = getValidateCodeType(request);
        if(null != validateCodeType){
            logger.info("校验请求{}中的验证码，验证码类型为：{}",request.getRequestURI(), validateCodeType);

            try{
                validateCodeProcessorHolder.findValidateCodeProcessor(validateCodeType).validateCode(new ServletWebRequest(request, response));
            }catch (CustomValidateCodeException ex){
                logger.error("验证码校验失败", ex);
                customAuthenticationFailedHandler.onAuthenticationFailure(request,response,ex);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType validateCodeType = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            for (String url : urlMaps.keySet()) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    validateCodeType = urlMaps.get(url);
                }
            }
        }
        return validateCodeType;
    }
}
