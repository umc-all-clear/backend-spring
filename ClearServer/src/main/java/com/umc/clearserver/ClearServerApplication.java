package com.umc.clearserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ClearServerApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ClearServerApplication.class).run(args);
		//SpringApplication.run(ClearServerApplication.class, args);
	}
}