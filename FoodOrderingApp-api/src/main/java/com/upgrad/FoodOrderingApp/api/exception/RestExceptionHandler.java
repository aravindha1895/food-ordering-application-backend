package com.upgrad.FoodOrderingApp.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(AuthorizationFailedException.class)
	public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe,
			WebRequest request) {

		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
				HttpStatus.FORBIDDEN);

	}
	@ExceptionHandler(CouponNotFoundException.class)
	public ResponseEntity<ErrorResponse> couponCodeNotFoundException(CouponNotFoundException exe,
			WebRequest request) {

		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
				HttpStatus.NOT_FOUND);

	}
	@ExceptionHandler(SignUpRestrictedException.class)
	public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exe, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exe,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
				HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler(UpdateCustomerException.class)
	public ResponseEntity<ErrorResponse> updateCustomerFailedFailedException(UpdateCustomerException exe,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> nullPointerException(NullPointerException exe,
			WebRequest request)  {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getLocalizedMessage()).message(exe.getMessage()),
				HttpStatus.BAD_REQUEST);
	}
}
