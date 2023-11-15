package com.StockAppBackend.fullstackbackend.controller;


import com.StockAppBackend.fullstackbackend.entity.Supplier;
import com.StockAppBackend.fullstackbackend.service.map.SupplierServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class SupplierController {

    private final SupplierServiceImpl supplierService;

    public SupplierController(SupplierServiceImpl supplierService) {
        this.supplierService = supplierService;
    }


    @PostMapping("/supplier")
    Supplier newSupplier(@RequestBody Supplier newSupplier){
        return supplierService.save(newSupplier);
    }

    @GetMapping("/suppliers")
    List<Supplier> getAllUsers(){
        return supplierService.findAll();
    }

    @GetMapping("/supplier/{id}")
    Supplier getSupplierById(@PathVariable Long id){
        return supplierService.findById(id);
    }

    @PutMapping("/supplier/{id}")
    Supplier updateSupplier(@RequestBody Supplier newSupplier, @PathVariable Long id){
       return supplierService.update(newSupplier,id);
    }

    @DeleteMapping("/supplier/{id}")
    String deleteUser(@PathVariable Long id){
        supplierService.deleteById(id);
        return  "Supplier with id "+id+" has been deleted success";

    }
}
