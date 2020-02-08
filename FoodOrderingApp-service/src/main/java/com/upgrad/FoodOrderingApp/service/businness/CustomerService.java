package com.upgrad.FoodOrderingApp.service.businness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.FoodOrderingApp.service.dao.CutomerDAO;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;

@Service
public class CustomerService {

	@Autowired
	CutomerDAO cutomerDAO;
	
	@Autowired
	private PasswordCryptographyProvider passwordCryptographyProvider;

	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity registerCustomer(CustomerEntity customer) throws SignUpRestrictedException {
		if(isPhoneNumberExist(customer))
			throw new SignUpRestrictedException("SGR-001",
					"This contact number is already registered! Try other contact number.");
		if(!validateCustomer(customer))
			throw new SignUpRestrictedException("SGR-005",
					"Except last name all fields should be filled");
		if(!isValidEmailID(customer))
			throw new SignUpRestrictedException("SGR-002",
					"Invalid email-id format!");
		if(!isValidPhoneNumber(customer))
			throw new SignUpRestrictedException("SGR-003",
					"Invalid contact number!");
		if(!isValidPassword(customer))
			throw new SignUpRestrictedException("SGR-004",
					"Weak password!");
		String[] encryptedText = passwordCryptographyProvider.encrypt(customer.getPassword());
		customer.setSalt(encryptedText[0]);
		customer.setPassword(encryptedText[1]);
		return cutomerDAO.registerCustomer(customer);
			
	}

	private boolean isPhoneNumberExist(CustomerEntity customer) throws SignUpRestrictedException {
		CustomerEntity customerEntity = cutomerDAO.getUserByPhoneNumber(customer.getEmail());
		if (customerEntity != null) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateCustomer(CustomerEntity customer) {
		if(customer.getEmail().trim().equals("") || customer.getContactNumber().equals("") || customer.getFirstName().equals("") || customer.getPassword().equals(""))
			return false;
		return true;
	}
	private boolean isValidEmailID(CustomerEntity customer) {
		return true;
	}

	private boolean isValidPhoneNumber(CustomerEntity customer) {
		return true;
	}

	private boolean isValidPassword(CustomerEntity customer) {
		return true;
	}


}
