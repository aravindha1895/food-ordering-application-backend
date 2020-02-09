package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants() {
        return entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class).getResultList();
    }

    public List<RestaurantEntity> getAllRestaurantsByName(String restName) {
        return  entityManager.createNamedQuery("restbyName", RestaurantEntity.class).setParameter("restaurant_name", "%" + restName + "%").getResultList();
    }

    public RestaurantEntity getRestaurantById(String restUuid) {
        return entityManager.createNamedQuery("restByUuid", RestaurantEntity.class).setParameter("uuid", restUuid).getSingleResult();
    }

}
