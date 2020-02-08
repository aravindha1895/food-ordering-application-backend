package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;

@Repository
public class OrderDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public CouponEntity getCouponDetailsByName(String name) {
		try {
			return entityManager.createNamedQuery("getCouponDetailByName", CouponEntity.class)
					.setParameter("coupon_name", name).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}

	}
}
