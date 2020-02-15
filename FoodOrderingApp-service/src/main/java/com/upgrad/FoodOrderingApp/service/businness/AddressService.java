package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDAO;
import com.upgrad.FoodOrderingApp.service.dao.StateDAO;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class AddressService {

    @Autowired
    AddressDAO addressDAO;

    @Autowired
    StateDAO stateDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity address, String uuId) throws SaveAddressException {

        if(!isPinCodeValid(address.getPincode()))
            throw new SaveAddressException("SAR-002",
                    "Invalid pincode");


        StateEntity stateEntity = getStateById(uuId);

        if(stateEntity==null)
            throw new SaveAddressException("ANF-002",
                    "No state by this id");

        address.setStateEntity(stateEntity);
        return addressDAO.saveAddress(address);

    }

    /**
     *
     * util methods for validation checks
     *
     * on address entity
     *
     * */


    //this will return true is pin code is valid
    private boolean isPinCodeValid(String pinCode){
        Pattern digitPattern = Pattern.compile("\\d{6}");
        return digitPattern.matcher(pinCode).matches();
    }

    //this will return state if it's present in the database
    private StateEntity getStateById(String uuId){
        return stateDAO.getStateById(uuId);
    }
}
