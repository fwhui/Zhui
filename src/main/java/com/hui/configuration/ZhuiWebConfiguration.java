package com.hui.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hui.interceptor.LoginRequredInterceptor;
import com.hui.interceptor.PassportInterceptor;

@Component
public class ZhuiWebConfiguration extends WebMvcConfigurerAdapter{
	@Autowired
	PassportInterceptor passportInterceptor;
	
	@Autowired
	LoginRequredInterceptor loginRequredInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}
	
}
