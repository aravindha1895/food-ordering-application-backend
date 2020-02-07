package com.upgrad.FoodOrderingApp.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.upgrad.FoodOrderingApp.AuthenticationInterceptor;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter  {  

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new AuthenticationInterceptor());
    }
} 
