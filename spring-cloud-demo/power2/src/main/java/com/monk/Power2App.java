package com.monk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Power2App {

    public static void main(String[] args) {
        SpringApplication.run(Power2App.class, args);
    }
}
