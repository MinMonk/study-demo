package com.monk.app.signout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.app.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MonkLogoutSuccessHandler
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/30
 * @Version V1.0
 **/
public class MonkLogoutSuccessHandler implements LogoutSuccessHandler {

    public static final Logger logger = LoggerFactory.getLogger(MonkLogoutSuccessHandler.class);

    private String signOut;

    public MonkLogoutSuccessHandler(String signOut){
        this.signOut = signOut;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        logger.info("退出成功");

        if(StringUtils.isBlank(signOut)){
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
        }else{
            response.sendRedirect(signOut);
        }
    }
}
