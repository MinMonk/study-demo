package com.monk;

import com.ruleconfig.OrderConfig;
import com.ruleconfig.PowerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.monk.service"})
@EnableHystrix
@RibbonClients({
        @RibbonClient(name="SERVER-ORDER", configuration = OrderConfig.class),
        @RibbonClient(name="SERVER-POWER", configuration = PowerConfig.class)
})
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}
