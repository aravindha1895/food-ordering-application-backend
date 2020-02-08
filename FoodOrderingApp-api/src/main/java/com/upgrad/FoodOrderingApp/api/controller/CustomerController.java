package com.upgrad.FoodOrderingApp.api.controller;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@CrossOrigin
	@RequestMapping(value = "/customer/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SignupCustomerResponse> signUpCustomer(
			@RequestBody SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

		CustomerEntity customer = new CustomerEntity();

		customer.setUuid(UUID.randomUUID().toString());
		customer.setFirstName(signupCustomerRequest.getFirstName());
		customer.setLastName(signupCustomerRequest.getLastName());
		customer.setEmail(signupCustomerRequest.getEmailAddress());
		customer.setContactNumber(signupCustomerRequest.getContactNumber());
		customer.setPassword(signupCustomerRequest.getPassword());

		CustomerEntity result = customerService.registerCustomer(customer);

		return new ResponseEntity<SignupCustomerResponse>(
				new SignupCustomerResponse().id(result.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED"),
				HttpStatus.CREATED);
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization)
			throws AuthenticationFailedException {

		String[] authorizationArray;
		byte[] decode;
		String decodedText;
		String[] authArray;
		/*Decode base 64 encoded values*/
		try {
			authorizationArray = authorization.split("Basic ");
			decode = Base64.getDecoder().decode(authorizationArray[1]);
			decodedText = new String(decode);
			authArray = decodedText.split(":");
			if (authArray.length != 2)
				throw new Exception();
		} catch (Exception e) {
			throw new AuthenticationFailedException("ATH-003",
					"Incorrect format of decoded customer name and password");
		}

		CustomerAuthTokenEntity customerAuthToken = customerService.authenticate(authArray[0], authArray[1]);
		CustomerEntity customer = customerAuthToken.getUser();

		LoginResponse signinResponse = new LoginResponse().id(customer.getUuid()).firstName(customer.getFirstName())
				.lastName(customer.getLastName()).emailAddress(customer.getEmail())
				.contactNumber(customer.getContactNumber()).message("SIGNED IN SUCCESSFULLY");

		HttpHeaders headers = new HttpHeaders();
		headers.add("access-token", customerAuthToken.getAccessToken()); // Set access token in header
		return new ResponseEntity<LoginResponse>(signinResponse, headers, HttpStatus.OK);
	}
}
