package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
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

    public AddressEntity deleteAddressById(String addressId){
        return entityManager.createNamedQuery("deleteAddressById", AddressEntity.class).setParameter("addressuuid", addressId).getSingleResult();
    }

    public AddressEntity archiveAddressById(String addressId){
        return entityManager.createNamedQuery("archiveAddressById", AddressEntity.class).setParameter("addressuuid", addressId).getSingleResult();
    }

    public AddressEntity getAddressById(String addressId){
        return entityManager.createNamedQuery("getAddressById", AddressEntity.class).setParameter("addressuuid", addressId).getSingleResult();
    }

    public CustomerAddressEntity addEntrytoCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }
}