package com.monk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@EnablePersistence
public class Power1App {
    public static void main(String[] args) {
        SpringApplication.run(Power1App.class, args);
    }
}
