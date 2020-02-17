package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
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

	public CouponEntity getCouponById(String uuid) {
		try {
			return entityManager.createNamedQuery("getCouponById", CouponEntity.class).setParameter("couponUuid", uuid).getSingleResult();
		} catch(NoResultException nre) {
			return null;
		}
	}

	public OrderEntity postOrder(OrderEntity orderEntity) {
		entityManager.persist(orderEntity);
		return orderEntity;
	}

	public OrderItemEntity postOrderItem(OrderItemEntity orderItemEntity) {
		entityManager.persist(orderItemEntity);
		return orderItemEntity;
	}
}
