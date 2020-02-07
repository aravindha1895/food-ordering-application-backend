package com.upgrad.FoodOrderingApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.FoodOrderingApp.dao.PaymentDAO;
import com.upgrad.FoodOrderingApp.entity.PaymentEntity;

@Service
public class PaymentService {
	@Autowired
	PaymentDAO paymentDAO;
	
	public List<PaymentEntity> fetchAllPaymentMethods(){
		return paymentDAO.getAllPaymentMethods();
	}
}
