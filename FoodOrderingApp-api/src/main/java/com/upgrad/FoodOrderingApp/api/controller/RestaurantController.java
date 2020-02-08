package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    @RequestMapping(method = RequestMethod.GET, path = "/restaurant",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList>getAllRestaurants() {
        final List<RestaurantEntity> restaurantEntities = restaurantService.getAllRestaurants();
        List<RestaurantDetailsResponse> restaurantResponseDetails = new ArrayList<RestaurantDetailsResponse>();
        //List<CategoryList> categoriesList = new ArrayList<CategoryList>();

        for(int i=0; i < restaurantEntities.size(); i++) {
            restaurantResponseDetails.add(
                    new RestaurantDetailsResponse()
                            .id(restaurantEntities.get(i).getUuid())
                            .restaurantName(restaurantEntities.get(i).getRestaurant_name())
                            .photoURL(restaurantEntities.get(i).getPhoto_url())
                            .customerRating(restaurantEntities.get(i).getCustomer_rating())
                            .averagePrice(restaurantEntities.get(i).getAverage_price_for_two())
                            .numberCustomersRated(restaurantEntities.get(i).getNumber_of_customers_rated())
                            .address(new RestaurantDetailsResponseAddress()
                                    .id(restaurantEntities.get(i).getAddressEntity().getUuid())
                                    .flatBuildingName(restaurantEntities.get(i).getAddressEntity().getFlat_buil_number())
                                    .locality(restaurantEntities.get(i).getAddressEntity().getLocality())
                                    .city(restaurantEntities.get(i).getAddressEntity().getCity())
                                    .pincode(restaurantEntities.get(i).getAddressEntity().getPincode())
                                    .state(new RestaurantDetailsResponseAddressState().id(restaurantEntities.get(i).getAddressEntity().getStateEntity().getUuid())
                                            .stateName(restaurantEntities.get(i).getAddressEntity().getStateEntity().getState_name())
                                    )
                            )
            );
        }
        return new ResponseEntity<ArrayList>((ArrayList)restaurantResponseDetails, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList> getRestaurantsByName(@PathVariable("restaurant_name") final String restaurantName)throws RestaurantNotFoundException {
        String lowerrestaurantName = restaurantName.toLowerCase();
        final List<RestaurantEntity> restbyNameEntitiesList = restaurantService.getRestaurantsByName(lowerrestaurantName);
        List<RestaurantDetailsResponse> restbyNameDetailsResponseList = new ArrayList<RestaurantDetailsResponse>();
        for(int i=0; i < restbyNameEntitiesList.size(); i++) {
            restbyNameDetailsResponseList.add(
                    new RestaurantDetailsResponse()
                            .id(restbyNameEntitiesList.get(i).getUuid())
                            .restaurantName(restbyNameEntitiesList.get(i).getRestaurant_name())
                            .photoURL(restbyNameEntitiesList.get(i).getPhoto_url())
                            .customerRating(restbyNameEntitiesList.get(i).getCustomer_rating())
                            .averagePrice(restbyNameEntitiesList.get(i).getAverage_price_for_two())
                            .numberCustomersRated(restbyNameEntitiesList.get(i).getNumber_of_customers_rated())
                            .address(new RestaurantDetailsResponseAddress()
                                    .id(restbyNameEntitiesList.get(i).getAddressEntity().getUuid())
                                    .flatBuildingName(restbyNameEntitiesList.get(i).getAddressEntity().getFlat_buil_number())
                                    .locality(restbyNameEntitiesList.get(i).getAddressEntity().getLocality())
                                    .city(restbyNameEntitiesList.get(i).getAddressEntity().getCity())
                                    .pincode(restbyNameEntitiesList.get(i).getAddressEntity().getPincode())
                                    .state(new RestaurantDetailsResponseAddressState().id(restbyNameEntitiesList.get(i).getAddressEntity().getStateEntity().getUuid())
                                            .stateName(restbyNameEntitiesList.get(i).getAddressEntity().getStateEntity().getState_name())
                                    )
                            )
            );
        }
        return new ResponseEntity<ArrayList>((ArrayList)restbyNameDetailsResponseList, HttpStatus.OK);
    }

}
