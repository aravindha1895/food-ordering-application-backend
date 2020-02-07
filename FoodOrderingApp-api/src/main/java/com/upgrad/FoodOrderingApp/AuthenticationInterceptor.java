package com.upgrad.FoodOrderingApp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthenticationInterceptor implements HandlerInterceptor  {
	  @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		  System.out.println("--- pre method executed---");
		  return true;
	    }
	    @Override
	    public void postHandle( HttpServletRequest request, HttpServletResponse response,
	            Object handler, ModelAndView modelAndView) throws Exception {
	        System.out.println("---method executed---");
	    }
	    @Override
	    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
	            Object handler, Exception ex) throws Exception {
	        System.out.println("---Request Completed---");
	    }
}
