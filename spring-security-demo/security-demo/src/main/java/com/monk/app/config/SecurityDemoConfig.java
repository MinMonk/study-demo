package com.monk.app.config;

import com.monk.app.filter.CustomFilter;
import com.monk.app.interceptor.CustomTimeoutCallableProcessingInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityDemoConfig extends WebMvcConfigurerAdapter {

//    @Bean
    public FilterRegistrationBean registrationFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        CustomFilter customFilter = new CustomFilter();
        filterRegistrationBean.setFilter(customFilter);

        List<String> filterUrls = new ArrayList<String>();
        filterUrls.add("/user/*");
        filterRegistrationBean.setUrlPatterns(filterUrls);

        return filterRegistrationBean;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        CustomInterceptor customInterceptor = new CustomInterceptor();
//        registry.addInterceptor(customInterceptor);
//    }


    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(60 * 1000L);
        configurer.registerCallableInterceptors(new CustomTimeoutCallableProcessingInterceptor());

        configurer.setTaskExecutor(threadPoolTaskExecutor());
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setThreadNamePrefix("Child-Thread-");
        return threadPoolTaskExecutor;
    }
}
