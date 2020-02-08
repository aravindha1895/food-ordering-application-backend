package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CutomerDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public CustomerEntity registerCustomer(CustomerEntity customer) {
		entityManager.persist(customer);
		return customer;
	}

	public CustomerEntity getUserByPhoneNumber(final String phoneNumber) {
		try {
			return entityManager.createNamedQuery("customerByPhoneNumber", CustomerEntity.class)
					.setParameter("contact_number", phoneNumber).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public CustomerAuthTokenEntity getUserAuthToken(final String accessToken) {
		try {
			return entityManager.createNamedQuery("userAuthTokenByAccessToken", CustomerAuthTokenEntity.class)
					.setParameter("accessToken", accessToken).getSingleResult();
		} catch (NoResultException nre) {

			return null;
		}
	}

	public CustomerAuthTokenEntity getCustomerAuthEntityTokenByUUID(final String UUID) {
		try {
			return entityManager.createNamedQuery("userAuthTokenByUUID", CustomerAuthTokenEntity.class)
					.setParameter("uuid", UUID).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public CustomerEntity authenticateUser(final String phone, final String password) {
		try {
			return entityManager.createNamedQuery("authenticateUserQuery", CustomerEntity.class)
					.setParameter("contactNumber", phone).setParameter("password", password).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public CustomerAuthTokenEntity createAuthToken(final CustomerAuthTokenEntity customerAuthTokenEntity) {
		entityManager.persist(customerAuthTokenEntity);
		return customerAuthTokenEntity;
	}

	public CustomerAuthTokenEntity updateUserLogOut(final CustomerAuthTokenEntity customerAuthTokenEntity) {
		try {
			return entityManager.merge(customerAuthTokenEntity);
		} catch (NoResultException nre) {
			return null;
		}
	}

	public CustomerAuthTokenEntity updateLoginInfo(final CustomerAuthTokenEntity userAuthTokenEntity) {
		try {
			return entityManager.merge(userAuthTokenEntity);
		} catch (NoResultException nre) {
			return null;
		}
	}

	public CustomerEntity updateCustomerDetails(CustomerEntity customer) {
		try {
			return entityManager.merge(customer);

		} catch (NoResultException nre) {
			return null;
		}
	}

}
