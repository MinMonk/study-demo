package com.monk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class NacosConfigApp {

    public static void main(String[] args) throws Exception{
        ConfigurableApplicationContext ctx = SpringApplication.run(NacosConfigApp.class, args);
        while (true){
            String userName = ctx.getEnvironment().getProperty("user.name");
            String nickName = ctx.getEnvironment().getProperty("user.nick_name");
            String env = ctx.getEnvironment().getProperty("current.env");
            String extProperties = ctx.getEnvironment().getProperty("ext_properties");
            String global = ctx.getEnvironment().getProperty("global");
            String userInfo = ctx.getEnvironment().getProperty("user.info");
            System.out.println(String.format("env:[%s], userName:[%s], nickName:[%s]", new Object[]{env, userName, nickName}));
            System.out.println("extProperties:" + extProperties);
            System.out.println("global:" + global);
            System.out.println(userInfo);

            TimeUnit.SECONDS.sleep(3);
        }
    }
}
