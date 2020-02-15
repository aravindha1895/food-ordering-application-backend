package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity saveAddress(AddressEntity address) {
        entityManager.persist(address);
        return address;
    }

    public List<AddressEntity> getAllStates(String customerId) {
        try {
            return entityManager.createNamedQuery("getStateById", StateEntity.class).setParameter("customerId", customerId).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }
}
