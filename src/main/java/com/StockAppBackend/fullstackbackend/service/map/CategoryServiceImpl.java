package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.entity.Category;
import com.StockAppBackend.fullstackbackend.exception.SupplierNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.CategoryRepo;
import com.StockAppBackend.fullstackbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<Category> findAll() {
        List<Category> categories = categoryRepo.findAll();
        return categories;
    }

    @Override
    public Category findById(Long aLong) {
        return categoryRepo.findById(aLong).orElseThrow(()->new SupplierNotFoundException(aLong));
    }

    @Override
    public Category save(Category object) {
        return categoryRepo.save(object);
    }

    @Override
    public void delete(Category object) {
        categoryRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        categoryRepo.deleteById(aLong);
    }


    public Category update(Category newCategory, Long id){
        return categoryRepo.findById(id).map(category -> {

            category.setName(newCategory.getName());
            category.setDescription(newCategory.getDescription());


            return categoryRepo.save(category);
        }).orElseThrow(()->new SupplierNotFoundException(id));
    }


}
