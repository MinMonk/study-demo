package com.monk.app.session;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MonkCustomSessionInvalidStrategy
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/17
 * @Version V1.0
 **/
public class MonkCustomSessionInvalidStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        onSessionInvalid(request, response);
    }

    public MonkCustomSessionInvalidStrategy(String invalidUrl) {
        super(invalidUrl);
    }
}
