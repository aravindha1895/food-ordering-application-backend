package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity saveAddress(AddressEntity address) {
        entityManager.persist(address);
        return address;
    }

    /*public List<AddressEntity> getAllStates(String customerId) {
        try {
            return entityManager.createNamedQuery("getStateById", StateEntity.class).setParameter("customerId", customerId).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }*/

    public AddressEntity deleteAddressById(String addressId){
        return entityManager.createNamedQuery("deleteAddressById", AddressEntity.class).setParameter("addressuuid", addressId).getSingleResult();
    }

    public AddressEntity archiveAddressById(String addressId){
        return entityManager.createNamedQuery("archiveAddressById", AddressEntity.class).setParameter("addressuuid", addressId).getSingleResult();
    }

    public AddressEntity getAddressById(String addressId){
        return entityManager.createNamedQuery("getAddressById", AddressEntity.class).setParameter("addressuuid", addressId).getSingleResult();
    }
}