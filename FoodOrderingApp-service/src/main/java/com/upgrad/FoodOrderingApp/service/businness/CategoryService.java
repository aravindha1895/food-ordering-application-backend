package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public CategoryEntity getCategoryById(String uuid) {
        CategoryEntity categoryEntity = categoryDao.getCategoryById(uuid);
//        if(uuid == "") {
//
//        } else if(categoryEntity == null) {
//
//        } else {
//            return categoryEntity;
//        }
        return categoryEntity;
    }

}
