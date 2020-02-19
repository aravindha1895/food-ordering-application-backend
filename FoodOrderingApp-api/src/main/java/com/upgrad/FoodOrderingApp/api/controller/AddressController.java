package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.api.util.Validators;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AddressController {

    @Autowired
    StateService stateService;

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST,value = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> postAddress(@RequestHeader("authorization") final String accessToken, @RequestBody SaveAddressRequest saveAddressRequest) throws SaveAddressException, AuthorizationFailedException, UpdateCustomerException {

        String bearerToken = null;
        try {
            bearerToken = accessToken.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = accessToken;
        }

        if(Validators.checkForEmptyEntityField(saveAddressRequest)) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        AddressEntity newAddress = new AddressEntity();
        newAddress.setCity(saveAddressRequest.getCity());
        newAddress.setFlat_buil_number(saveAddressRequest.getFlatBuildingName());
        newAddress.setLocality(saveAddressRequest.getLocality());
        newAddress.setPincode(saveAddressRequest.getPincode());
        newAddress.setActive(1);
        newAddress.setUuid(UUID.randomUUID().toString());

        String stateUuid = saveAddressRequest.getStateUuid();

        AddressEntity savedAddress = addressService.saveAddress(newAddress, stateUuid, bearerToken);

        CustomerEntity customerEntity = customerService.getCustomerByToken(bearerToken);

        addressService.addEntrytoCustomerAddress(customerEntity, savedAddress);
        return new ResponseEntity<SaveAddressResponse>(
                new SaveAddressResponse().id(savedAddress.getUuid()).
                        status("ADDRESS SUCCESSFULLY REGISTERED"),
                HttpStatus.CREATED);

    }

    @CrossOrigin
    @GetMapping(value="/address/customer",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<AddressListResponse> retrieveAllAddressForUser(
            @RequestHeader("authorization") final String accessToken){

        String bearerToken = null;
        try {
            bearerToken = accessToken.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = accessToken;
        }

        CustomerEntity customerEntity = customerService.getCustomerByToken(bearerToken);
        AddressListResponse response = new AddressListResponse();
        for(AddressEntity addressEntity : customerEntity.getAddress()){
            response.addAddressesItem(new AddressList().
                    id(UUID.fromString(addressEntity.getUuid())).
                    city(addressEntity.getCity()).
                    flatBuildingName(addressEntity.getFlat_buil_number()).
                    locality(addressEntity.getLocality()).
                    city(addressEntity.getCity()).
                    pincode(addressEntity.getPincode()).
                    state(new AddressListState().
                            stateName(addressEntity.getStateEntity().getState_name()).
                            id(UUID.fromString(addressEntity.getStateEntity().getUuid()))));
        }

        return new ResponseEntity<AddressListResponse>(response,HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping(value="/address/{address_id}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<DeleteAddressResponse> deleteAddressById(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable final String address_id) throws AddressNotFoundException {

        String bearerToken = null;
        try {
            bearerToken = accessToken.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = accessToken;
        }

        AddressEntity deletedEntity = addressService.deleteAddressById(address_id);

        return new ResponseEntity<DeleteAddressResponse>(
                new DeleteAddressResponse().id(UUID.fromString(deletedEntity.getUuid())).
                        status("ADDRESS DELETED SUCCESSFULLY"),
                HttpStatus.OK);    }

    @CrossOrigin
    @GetMapping(value="/states",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<StatesListResponse> retrieveAllStates(){
        List<StateEntity> stateEntities = stateService.fetchAllStates();
        StatesListResponse response = new StatesListResponse();
        for(StateEntity stateEntity: stateEntities) {
            response.addStatesItem(new StatesList()
                    .id(UUID.fromString(stateEntity.getUuid()))
                    .stateName(stateEntity.getState_name()));
        }
        return new ResponseEntity<StatesListResponse>(response,HttpStatus.OK);
    }
}
