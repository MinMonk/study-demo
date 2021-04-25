package com.monk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

@SpringBootApplication
@EnableZuulProxy
public class ZuulServer8002 {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServer8002.class, args);
    }
}
