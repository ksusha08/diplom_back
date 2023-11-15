package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.entity.Supplier;
import com.StockAppBackend.fullstackbackend.exception.SupplierNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.SupplierRepo;
import com.StockAppBackend.fullstackbackend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepo supplierRepo;


    @Override
    public List<Supplier> findAll() {
        List<Supplier> suppliers = supplierRepo.findAll();
        return suppliers;
    }

    @Override
    public Supplier findById(Long aLong) {
        return supplierRepo.findById(aLong).orElseThrow(()->new SupplierNotFoundException(aLong));
    }

    @Override
    public Supplier save(Supplier object) {
        return supplierRepo.save(object);
    }

    @Override
    public void delete(Supplier object) {
        supplierRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        supplierRepo.deleteById(aLong);
    }


    public Supplier update(Supplier newSupplier, Long id){
        return supplierRepo.findById(id).map(supplier -> {
            supplier.setName(newSupplier.getName());
            supplier.setEmail(newSupplier.getEmail());
            supplier.setAddress(newSupplier.getAddress());
            supplier.setCoefficient(newSupplier.getCoefficient());
            return supplierRepo.save(supplier);
        }).orElseThrow(()->new SupplierNotFoundException(id));
    }
}
