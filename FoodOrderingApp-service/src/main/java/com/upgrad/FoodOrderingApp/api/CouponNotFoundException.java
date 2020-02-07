package com.upgrad.FoodOrderingApp.api;

public class CouponNotFoundException extends Exception {
	private String code;
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CouponNotFoundException(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

}
