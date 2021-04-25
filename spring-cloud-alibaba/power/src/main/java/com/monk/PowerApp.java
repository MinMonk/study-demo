package com.monk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PowerApp {
    public static void main(String[] args) {
        SpringApplication.run(PowerApp.class, args);
    }
}
