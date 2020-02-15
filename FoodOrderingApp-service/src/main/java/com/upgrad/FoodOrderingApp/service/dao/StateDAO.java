package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StateEntity> getAllStates() {
        return entityManager.createNamedQuery("getAllStates").getResultList();
    }
}
