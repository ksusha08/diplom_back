package com.StockAppBackend.fullstackbackend.controller;



import com.StockAppBackend.fullstackbackend.entity.Category;
import com.StockAppBackend.fullstackbackend.service.map.CategoryServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/category")
    Category newCategory(@RequestBody Category newCategory){
        return categoryService.save(newCategory);
    }

    @GetMapping("/categories")
    List<Category> getAllCategories(){
        return categoryService.findAll();
    }

    @GetMapping("/category/{id}")
    Category getCategoryById(@PathVariable Long id){
        return categoryService.findById(id);
    }



    @PutMapping("/category/{id}")
    Category updateCategory(@RequestBody Category newCategory, @PathVariable Long id){
        return categoryService.update(newCategory,id);
    }

    @DeleteMapping("/category/{id}")
    String deleteCategory(@PathVariable Long id){
        categoryService.deleteById(id);
        return  "Supplier with id "+id+" has been deleted success";

    }
}