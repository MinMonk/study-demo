package com.monk.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.app.propertites.SecurityProperties;
import com.monk.app.support.SimpleResponse;
import com.monk.app.validate.bean.LoginResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CustomAuthenticationFailedHandler
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
@Component("customAuthenticationFailedHandler")
public class CustomAuthenticationFailedHandler extends SimpleUrlAuthenticationFailureHandler {

    private final static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailedHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("登陆失败");
        if(LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginType())){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
        }else{
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
