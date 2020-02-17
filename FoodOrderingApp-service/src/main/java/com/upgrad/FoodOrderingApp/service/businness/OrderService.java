package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;

import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

	@Autowired
	OrderDAO couponDAO;

	@Autowired
	CutomerDAO customerDAO;

	@Autowired
	RestaurantDao restaurantDao;

	@Autowired
	PaymentDAO paymentDAO;

	@Autowired
	AddressDAO addressDAO;

	public CouponEntity getCouponDetailByName(String name) throws CouponNotFoundException, AuthorizationFailedException {
		if (name.trim().equals(""))
			throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
		CouponEntity couponEntity = couponDAO.getCouponDetailsByName(name);
		if(couponEntity == null)
			throw new CouponNotFoundException("CPF-001","No coupon by this name");
		return couponEntity;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderEntity postOrder(OrderEntity orderEntity, String restaurantUuid, String paymentUuid, String couponUuid, String addressUuid, String authorization) throws AuthorizationFailedException, RestaurantNotFoundException,PaymentMethodNotFoundException,CouponNotFoundException,AddressNotFoundException {
		CustomerAuthTokenEntity customerAuthTokenEntity = customerDAO.getUserAuthToken(authorization);
		CustomerEntity customerEntity = customerAuthTokenEntity.getUser();
		orderEntity.setCustomerEntity(customerEntity);
		RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(restaurantUuid);
		orderEntity.setRestaurantEntity(restaurantEntity);
		PaymentEntity paymentEntity = paymentDAO.getPaymentById(paymentUuid);
		orderEntity.setPaymentEntity(paymentEntity);
		CouponEntity couponEntity = couponDAO.getCouponById(couponUuid);
		orderEntity.setCouponEntity(couponEntity);
		AddressEntity addressEntity = addressDAO.getAddressById(addressUuid);
		orderEntity.setAddressEntity(addressEntity);
		if(customerAuthTokenEntity == null) {
			throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
		} else if(customerAuthTokenEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
		} else if(customerAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
			throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
		} else if(couponEntity == null) {
			throw new CouponNotFoundException("CPF-002", "No coupon by this id");
		} else if(addressEntity == null) {
			throw new AddressNotFoundException("ANF-003","No address by this id");
		} else if(paymentEntity == null) {
			throw new PaymentMethodNotFoundException("PNF-002","No payment method found by this id");
		} else if(restaurantEntity == null) {
			throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
		} else {
			return couponDAO.postOrder(orderEntity);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderItemEntity postOrderItem(OrderItemEntity orderItemEntity) {
		return couponDAO.postOrderItem(orderItemEntity);
	}
}
