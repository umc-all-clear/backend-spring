package com.umc.clearserver.clearserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ClearServerApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.properties,"
			+ "classpath:aws.properties";
	public static void main(String[] args) {
		new SpringApplicationBuilder(ClearServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
		//SpringApplication.run(ClearServerApplication.class, args);
	}
}
