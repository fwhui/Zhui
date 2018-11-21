package com.hui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class ZhuiApplication extends SpringBootServletInitializer{


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ZhuiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ZhuiApplication.class, args);
	}
}
