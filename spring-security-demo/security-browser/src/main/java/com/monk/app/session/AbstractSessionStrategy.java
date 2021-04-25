package com.monk.app.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.app.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName AbstractSessionStrategy
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/17
 * @Version V1.0
 **/
public class AbstractSessionStrategy {

    public static final Logger logger = LoggerFactory.getLogger(AbstractSessionStrategy.class);

    private String directUrl;

    private Boolean createNewSession = true;


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private ObjectMapper objectMapper = new ObjectMapper();

    public AbstractSessionStrategy(String invalidUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidUrl), "URL must start with '/' or with 'http(s)'. ");
        this.directUrl = invalidUrl;
    }

    protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if(createNewSession){
            request.getSession();
        }

        String sourceUri = request.getRequestURI();
        String targetUrl = "";

        if (StringUtils.endsWith(sourceUri, ".html")) {
            targetUrl = directUrl + ".html";
            logger.info("session失效，跳转到{}", targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            StringBuilder msg = new StringBuilder("session失效");
            if(isConcurrency()){
                msg.append(", 有可能是并发登录导致的。");
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            SimpleResponse simpleResponse = new SimpleResponse(msg);
            response.getWriter().write(objectMapper.writeValueAsString(simpleResponse));
        }
    }

    protected Boolean isConcurrency(){
        return false;
    }

    public void setCreateNewSession(Boolean createNewSession) {
        this.createNewSession = createNewSession;
    }
}
