package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.entity.Category;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.exception.ItemNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.ItemRepo;
import com.StockAppBackend.fullstackbackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private final CategoryServiceImpl categoryService;

    public ItemServiceImpl(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public List<Item> findAll() {
        List<Item> items = itemRepo.findAll();
        return items;
    }

    @Override
    public Item findById(Long aLong) {
        return itemRepo.findById(aLong).orElseThrow(()->new ItemNotFoundException(aLong));
    }


    @Override
    public Item save(Item object) {
        return itemRepo.save(object);
    }

    @Override
    public void delete(Item object) {
        itemRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        itemRepo.deleteById(aLong);
    }


    public Item updateAll(Item newItem, Long id, Long categoryId){

        return itemRepo.findById(id).map(item -> {
            item.setName(newItem.getName());
            item.setVendoreCode(newItem.getVendoreCode());
            item.setDiscountPrice(newItem.getDiscountPrice());
            item.setNumber(newItem.getNumber());
            item.setPhotos(newItem.getPhotos());

            if(categoryId != null) {
                Category category = categoryService.findById(categoryId);
                item.setCategory(category);
            }

            return itemRepo.save(item);
        }).orElseThrow(()->new ItemNotFoundException(id));
    }

    public Item update(Item newItem, Long id){

        return itemRepo.findById(id).map(item -> {
            item.setName(newItem.getName());
            item.setVendoreCode(newItem.getVendoreCode());
            item.setDiscountPrice(newItem.getDiscountPrice());
            item.setNumber(newItem.getNumber());
            item.setPhotos(newItem.getPhotos());


            return itemRepo.save(item);
        }).orElseThrow(()->new ItemNotFoundException(id));
    }

    public List<Item> findByName(String name) {
        List<Item> items = itemRepo.findByNameContainingIgnoreCase(name);
        return items;
    }

    public List<Item> findByCategoryId(Long id) {
        List<Item> itemsByCategory = itemRepo.findByCategoryId(id);
        return itemsByCategory;
    }


}
