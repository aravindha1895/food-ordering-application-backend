package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList> getAllCategories() {
        List<CategoryEntity> categoryEntity = categoryService.getAllCategories();
        List<CategoryListResponse> categoryListResponse = new ArrayList<CategoryListResponse>();
        for (int i=0; i < categoryEntity.size(); i++) {
            categoryListResponse.add(new CategoryListResponse()
                    .id(UUID.fromString(categoryEntity.get(i).getUuid()))
                    .categoryName(categoryEntity.get(i).getCategory_name())
            );
        }
        return new ResponseEntity<ArrayList>((ArrayList)categoryListResponse,HttpStatus.OK);
    }
}
