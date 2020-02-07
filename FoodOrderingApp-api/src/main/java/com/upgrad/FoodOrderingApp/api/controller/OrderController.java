package com.upgrad.FoodOrderingApp.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderResponse;
import com.upgrad.FoodOrderingApp.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;

@RestController
public class OrderController {
	@Autowired
	OrderService orderService;
	
	@RequestMapping(method=RequestMethod.GET,value="/order/coupon/{coupon_name}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable("coupon_name") String couponName) throws CouponNotFoundException{
		CouponEntity couponEntity =  orderService.getCouponDetailByName(couponName);
		CouponDetailsResponse couponDetailsResponse= new CouponDetailsResponse();
		couponDetailsResponse.setCouponName(couponEntity.getCouponName());
		couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
		couponDetailsResponse.setPercent(couponEntity.getPercent());
		return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/order", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SaveOrderResponse> postOrder(@RequestBody SaveOrderRequest saveOrderRequest ){
		
		
		return null;
	}
}
