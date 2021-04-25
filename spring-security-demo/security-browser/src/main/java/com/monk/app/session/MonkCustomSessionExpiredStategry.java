package com.monk.app.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @ClassName MonkCustomSessionExpiredStategry
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/17
 * @Version V1.0
 **/
public class MonkCustomSessionExpiredStategry extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

    public MonkCustomSessionExpiredStategry(String invalidUrl) {
        super(invalidUrl);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent eventØ) throws IOException, ServletException {
//        eventØ.getResponse().setContentType("application/json;charset=UTF-8");
//        eventØ.getResponse().getWriter().write("并发登录导致session失效");
        onSessionInvalid(eventØ.getRequest(), eventØ.getResponse());
    }

    @Override
    protected Boolean isConcurrency() {
        return true;
    }
}
