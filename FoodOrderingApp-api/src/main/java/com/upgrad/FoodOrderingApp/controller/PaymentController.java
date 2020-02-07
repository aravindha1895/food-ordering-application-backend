package com.upgrad.FoodOrderingApp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.PaymentService;

@RestController
public class PaymentController {

	@Autowired
	PaymentService paymentService;

	@RequestMapping(method=RequestMethod.GET,value="/payment")
	public ResponseEntity<PaymentListResponse> retrievePaymentMethods(){
		List<PaymentEntity> paymentEntities = paymentService.fetchAllPaymentMethods();
		PaymentListResponse response = new PaymentListResponse();
		for(PaymentEntity paymentEntity: paymentEntities) {
			response.addPaymentMethodsItem(new PaymentResponse().id(UUID.fromString(paymentEntity.getUuid())).paymentName(paymentEntity.getPaymentName()));
		}
		return new ResponseEntity<PaymentListResponse>(response,HttpStatus.OK);
	}

}
