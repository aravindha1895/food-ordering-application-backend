package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.api.util.Validators;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AddressController {

    @Autowired
    StateService stateService;

    @Autowired
    AddressService addressService;

    @CrossOrigin
    @PostMapping(value = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<SaveAddressResponse> signUpCustomer(
            @RequestHeader("authorization") final String accessToken,
            @RequestBody SaveAddressRequest saveAddressRequest) throws SaveAddressException {

        String bearerToken = null;
        try {
            bearerToken = accessToken.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = accessToken;
        }

        if(Validators.checkForEmptyEntityField(saveAddressRequest))
            throw new SaveAddressException("SAR-001",
                    "No field can be empty");

        AddressEntity newAddress = new AddressEntity();
        newAddress.setCity(saveAddressRequest.getCity());
        newAddress.setFlat_buil_number(saveAddressRequest.getFlatBuildingName());
        newAddress.setLocality(saveAddressRequest.getLocality());
        newAddress.setPincode(saveAddressRequest.getPincode());
        newAddress.setActive(1);

        AddressEntity savedAddress = addressService.saveAddress(newAddress, saveAddressRequest.getStateUuid());

        return new ResponseEntity<SaveAddressResponse>(
                new SaveAddressResponse().id(savedAddress.getUuid()).
                        status("ADDRESS SUCCESSFULLY REGISTERED"),
                HttpStatus.CREATED);

    }

    @CrossOrigin
    @GetMapping(value="/states",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<StatesListResponse> retrieveAllStates(){
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
