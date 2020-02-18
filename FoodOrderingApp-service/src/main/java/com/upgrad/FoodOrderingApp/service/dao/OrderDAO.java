package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;

import java.util.List;

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

    public CouponEntity getCouponDetailsById(int id) {
        try {
            return entityManager.createNamedQuery("getCouponDetailById", CouponEntity.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

	public List<OrderEntity> fetchOrdersByCustomer(long customerId) {
		try {
			return entityManager.createNamedQuery("fetchAllOrders", OrderEntity.class)
					.setParameter("customerId", customerId).getResultList();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public OrderItemEntity postOrderItem(OrderItemEntity orderItemEntity) {
		entityManager.persist(orderItemEntity);
		return orderItemEntity;
	}

	public OrderItemEntity fetchItemDetails(String orderId){
        try {
            return entityManager.createNamedQuery("fetchItemDetails", OrderItemEntity.class)
                    .setParameter("order_id", orderId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<OrderEntity> fetchOrderByAddress(String addressId){
        try {
            return entityManager.createNamedQuery("fetchOrderByAddress", OrderEntity.class)
                    .setParameter("address_id", addressId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }


}
