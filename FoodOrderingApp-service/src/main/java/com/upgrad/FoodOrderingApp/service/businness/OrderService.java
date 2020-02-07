package com.upgrad.FoodOrderingApp.service.businness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.FoodOrderingApp.dao.OrderDAO;
import com.upgrad.FoodOrderingApp.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;

@Service
public class OrderService {

	@Autowired
	OrderDAO couponDAO;
	public CouponEntity getCouponDetailByName(String name) throws CouponNotFoundException {
		if (name.trim().equals(""))
			throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
		CouponEntity couponEntity = couponDAO.getCouponDetailsByName(name);
		if(couponEntity == null)
			throw new CouponNotFoundException("CPF-001","No coupon by this name");
		return couponEntity;
	}
}
