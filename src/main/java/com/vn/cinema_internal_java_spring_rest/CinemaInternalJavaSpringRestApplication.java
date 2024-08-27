package com.vn.cinema_internal_java_spring_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// disable security
// @SpringBootApplication(exclude = {
// org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
// org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
// })
public class CinemaInternalJavaSpringRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaInternalJavaSpringRestApplication.class, args);
	}

}
