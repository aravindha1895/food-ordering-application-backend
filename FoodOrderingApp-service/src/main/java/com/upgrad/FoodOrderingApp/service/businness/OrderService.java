package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.FoodOrderingApp.service.dao.OrderDAO;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;

import java.util.List;

@Service
public class OrderService {

	@Autowired
	OrderDAO orderDAO;

	public CouponEntity getCouponDetailByName(String name) throws CouponNotFoundException, AuthorizationFailedException {
		if (name.trim().equals(""))
			throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
		CouponEntity couponEntity = orderDAO.getCouponDetailsByName(name);
		if(couponEntity == null)
			throw new CouponNotFoundException("CPF-001","No coupon by this name");
		return couponEntity;
	}

    public CouponEntity getCouponDetailsById(int id) throws CouponNotFoundException, AuthorizationFailedException {
       return orderDAO.getCouponDetailsById(id);
    }

	public List<OrderEntity> retrieveAllOrders(CustomerEntity customer){
	    return orderDAO.fetchOrdersByCustomer(customer.getId());
    }

    public OrderItemEntity fetchItemDetails(String orderId){
	    return orderDAO.fetchItemDetails(orderId);
    }
}
