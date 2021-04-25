package com.monk.app.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;

import java.util.concurrent.Callable;

/**
 * @ClassName CustomTimeoutCallableProcessingInterceptor
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
public class CustomTimeoutCallableProcessingInterceptor implements CallableProcessingInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(CustomTimeoutCallableProcessingInterceptor.class);

    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception {

    }

    @Override
    public <T> void preProcess(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception {

    }

    @Override
    public <T> void postProcess(NativeWebRequest nativeWebRequest, Callable<T> callable, Object o) throws Exception {

    }

    @Override
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
        logger.info("toime out ----");
        return new TimeoutCallableProcessingInterceptor();
    }

    @Override
    public <T> void afterCompletion(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception {

    }
}
