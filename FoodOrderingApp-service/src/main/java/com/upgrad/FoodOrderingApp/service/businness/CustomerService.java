package com.upgrad.FoodOrderingApp.service.businness;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.FoodOrderingApp.service.dao.CutomerDAO;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

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
		if (!isValidPassword(customer.getPassword()))
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
		boolean isDataAlreadyExists = false;
		CustomerAuthTokenEntity customerAuthTokenEntity = null;
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
			customerAuthTokenEntity = customerDAO.getCustomerAuthEntityTokenByUUID(customerEntity.getUuid());
			if (customerAuthTokenEntity != null) {
				isDataAlreadyExists = true;
			} else {
				customerAuthTokenEntity = new CustomerAuthTokenEntity();
			}
			customerAuthTokenEntity.setUser(customerEntity);
			final ZonedDateTime now = ZonedDateTime.now();
			final ZonedDateTime expiresAt = now.plusHours(8);
			customerAuthTokenEntity
					.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
			customerAuthTokenEntity.setLoginAt(now);
			customerAuthTokenEntity.setExpiresAt(expiresAt);
			customerAuthTokenEntity.setUuid(customerEntity.getUuid());
			customerAuthTokenEntity.setLogoutAt(null);
			if (isDataAlreadyExists)
				customerDAO.createAuthToken(customerAuthTokenEntity);
			else
				customerDAO.updateLoginInfo(customerAuthTokenEntity);
		} else {
			throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
		}
		return customerAuthTokenEntity;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerAuthTokenEntity signOutService(String accessToken) throws AuthorizationFailedException {
		CustomerAuthTokenEntity customerAuthTokenEntity = null;
		// check interceptor for validity check
		customerAuthTokenEntity = customerDAO.getUserAuthToken(accessToken);
		final ZonedDateTime now = ZonedDateTime.now();
		customerAuthTokenEntity.setLogoutAt(now);
		customerAuthTokenEntity = customerDAO.updateUserLogOut(customerAuthTokenEntity);
		return customerAuthTokenEntity;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity updateCustomer(CustomerEntity updatedEntity, String accessToken)
			throws UpdateCustomerException, AuthorizationFailedException {
		if (updatedEntity.getFirstName().trim().length() == 0)
			throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
		CustomerAuthTokenEntity customerAuthTokenEntity = null;
		customerAuthTokenEntity = customerDAO.getUserAuthToken(accessToken);
		CustomerEntity existingRecord = customerAuthTokenEntity.getUser();
		existingRecord.setFirstName(updatedEntity.getFirstName());
		existingRecord.setLastName(updatedEntity.getLastName());
		customerDAO.updateCustomerDetails(existingRecord);
		return existingRecord;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, String accessToken)
			throws UpdateCustomerException, AuthorizationFailedException {
		if (!isValidPassword(newPassword))
			throw new UpdateCustomerException("UCR-001", "Weak password!");
		CustomerAuthTokenEntity customerAuthTokenEntity = null;
		customerAuthTokenEntity = customerDAO.getUserAuthToken(accessToken);
		CustomerEntity existingRecord = customerAuthTokenEntity.getUser();
		final String encryptedOldPassword = cryptographyProvider.encrypt(oldPassword, existingRecord.getSalt());
		if (!encryptedOldPassword.equals(existingRecord.getPassword()))
			throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
		final String encryptedNewPassword = cryptographyProvider.encrypt(newPassword, existingRecord.getSalt());
		existingRecord.setPassword(encryptedNewPassword);
		customerDAO.updateCustomerDetails(existingRecord);
		return existingRecord;
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
		if (customer.getEmail().trim().length() == 0 || customer.getContactNumber().length() == 0
				|| customer.getFirstName().length() == 0 || customer.getPassword().length() == 0)
			return false;
		return true;
	}

	private boolean isValidEmailID(CustomerEntity customer) {
		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(customer.getEmail());
		return matcher.find();
	}

	private boolean isValidPhoneNumber(CustomerEntity customer) {
		Pattern pattern = Pattern.compile("^[1-9]+\\d{9}$");
		Matcher matcher = pattern.matcher(customer.getContactNumber());
		return matcher.matches();
	}

	private boolean isValidPassword(String password) {
		Pattern p1 = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).*$");
		Matcher matcher = p1.matcher(password);
		return password.length() >= 8 && matcher.matches();
	}

	/**
	 * This function checks if the customer is logged in or if the token is expired
	 * or not. FOr the endpoints that require protection of login checks, please
	 * configure those endpoints in AppConfig.java which will inturn use this
	 * function to check validity
	 * 
	 * @param accessToken
	 * @return boolean if valid customer
	 * @throws AuthorizationFailedException
	 */
	public boolean checkCustomerEntityValidity(String accessToken) throws AuthorizationFailedException {
		CustomerAuthTokenEntity customerAuthTokenEntity = null;
		// check user sign in or not
		customerAuthTokenEntity = customerDAO.getUserAuthToken(accessToken);
		if (customerAuthTokenEntity != null) {
			if (customerAuthTokenEntity.getLogoutAt() != null)
				throw new AuthorizationFailedException("ATHR-002",
						"Customer is logged out. Log in again to access this endpoint.");
			else if (customerAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now()))
				throw new AuthorizationFailedException("ATHR-003",
						"Your session is expired. Log in again to access this endpoint.");
		} else {
			// if user is not sign in then throws exception
			throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
		}
		return true;
	}
}
