package com.upgrad.FoodOrderingApp.service.businness;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.FoodOrderingApp.service.dao.CutomerDAO;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;

@Service
public class CustomerService {

	@Autowired
	CutomerDAO customerDAO;

	@Autowired
	private PasswordCryptographyProvider cryptographyProvider;

	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity registerCustomer(CustomerEntity customer) throws SignUpRestrictedException {
		/* Validation checks */
		if (isPhoneNumberExist(customer))
			throw new SignUpRestrictedException("SGR-001",
					"This contact number is already registered! Try other contact number.");
		if (!validateCustomer(customer))
			throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
		if (!isValidEmailID(customer))
			throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
		if (!isValidPhoneNumber(customer))
			throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
		if (!isValidPassword(customer))
			throw new SignUpRestrictedException("SGR-004", "Weak password!");
		/* Encrypt password */
		String[] encryptedText = cryptographyProvider.encrypt(customer.getPassword());
		customer.setSalt(encryptedText[0]);
		customer.setPassword(encryptedText[1]);
		return customerDAO.registerCustomer(customer);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerAuthTokenEntity authenticate(final String phone, final String password)
			throws AuthenticationFailedException {
		CustomerAuthTokenEntity CustomerAuthTokenEntity = null;
		CustomerEntity customerEntity = customerDAO.getUserByPhoneNumber(phone);
		// check contact number exist
		if (customerEntity == null) {
			throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
		}
		// encrypt password
		final String encryptedPassword = cryptographyProvider.encrypt(password, customerEntity.getSalt());

		// send phone and encrypted password to customerDAO
		customerEntity = customerDAO.authenticateUser(phone, encryptedPassword);

		if (customerEntity != null) {// if userName and password match
			String uuid = customerEntity.getUuid();
			// Geneate authention token
			JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			CustomerAuthTokenEntity = new CustomerAuthTokenEntity();
			CustomerAuthTokenEntity.setUser(customerEntity);
			final ZonedDateTime now = ZonedDateTime.now();
			final ZonedDateTime expiresAt = now.plusHours(8);
			CustomerAuthTokenEntity
					.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
			CustomerAuthTokenEntity.setLoginAt(now);
			CustomerAuthTokenEntity.setExpiresAt(expiresAt);
			CustomerAuthTokenEntity.setUuid(customerEntity.getUuid());
			customerDAO.createAuthToken(CustomerAuthTokenEntity);
		} else {
			throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
		}
		return CustomerAuthTokenEntity;
	}

	private boolean isPhoneNumberExist(CustomerEntity customer) throws SignUpRestrictedException {
		CustomerEntity customerEntity = customerDAO.getUserByPhoneNumber(customer.getContactNumber());
		if (customerEntity != null) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateCustomer(CustomerEntity customer) {
		if (customer.getEmail().trim().equals("") || customer.getContactNumber().equals("")
				|| customer.getFirstName().equals("") || customer.getPassword().equals(""))
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
