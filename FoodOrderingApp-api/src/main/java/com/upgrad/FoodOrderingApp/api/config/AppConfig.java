package com.upgrad.FoodOrderingApp.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.upgrad.FoodOrderingApp.api.interceptor.AuthenticationInterceptor;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

	String[] pathPatternToIntercept = { "/order/coupon/{coupon_name}", "/order" };
	List<String> pathsToIntercept = new ArrayList<String>(Arrays.asList(pathPatternToIntercept));

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns(pathsToIntercept);
	}
}
