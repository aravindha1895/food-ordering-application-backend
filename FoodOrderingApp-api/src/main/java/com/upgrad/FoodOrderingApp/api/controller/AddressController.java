package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    StateService stateService;

    @CrossOrigin
    @GetMapping(value="/states",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> retrieveAllStates(){
        List<StateEntity> stateEntities = stateService.fetchAllStates();
        StatesListResponse response = new StatesListResponse();
        for(StateEntity stateEntity: stateEntities) {
            response.addStatesItem(new StatesList()
                    .id(stateEntity.getUuid())
                    .stateName(stateEntity.getState_name()));
        }
        return new ResponseEntity<StatesListResponse>(response,HttpStatus.OK);
    }
}
