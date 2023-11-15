package com.StockAppBackend.fullstackbackend.controller;

import com.StockAppBackend.fullstackbackend.entity.Category;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.service.map.StorehouseServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class StorehouseController {

    private final StorehouseServiceImpl storehouseService;

    public StorehouseController(StorehouseServiceImpl storehouseService) {
        this.storehouseService = storehouseService;
    }

    @PostMapping("/storehouse")
    Storehouse newStorehouse(@RequestBody Storehouse newStorehouse){
        return storehouseService.save(newStorehouse);
    }

    @GetMapping("/storehouses")
    List<Storehouse> getAllStorehouses(){
        return storehouseService.findAll();
    }

    @GetMapping("/storehouse/{id}")
    Storehouse getStorehouseById(@PathVariable Long id){
        return storehouseService.findById(id);
    }

    @PutMapping("/storehouse/{id}")
    Storehouse updateStorehouse(@RequestBody Storehouse newStorehouse, @PathVariable Long id){
        return storehouseService.update(newStorehouse,id);
    }

    @DeleteMapping("/storehouse/{id}")
    String deleteStorehouse(@PathVariable Long id){
        storehouseService.deleteById(id);
        return  "Supplier with id "+id+" has been deleted success";

    }
}
