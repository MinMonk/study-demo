package com.monk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.servlet")
public class Web3Application {

	public static void main(String[] args) {
		SpringApplication.run(Web3Application.class, args);
	}

}
