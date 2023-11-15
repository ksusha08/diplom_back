package com.StockAppBackend.fullstackbackend.service.map;

import com.StockAppBackend.fullstackbackend.entity.Category;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.exception.StorehouseNotFoundException;
import com.StockAppBackend.fullstackbackend.exception.SupplierNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.CategoryRepo;
import com.StockAppBackend.fullstackbackend.repo.StorehouseRepo;
import com.StockAppBackend.fullstackbackend.service.StorehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorehouseServiceImpl implements StorehouseService {

    @Autowired
    private StorehouseRepo storehouseRepo;

    @Override
    public List<Storehouse> findAll() {
        List<Storehouse> storehouses = storehouseRepo.findAll();
        return storehouses;
    }

    @Override
    public Storehouse findById(Long aLong) {
        return storehouseRepo.findById(aLong).orElseThrow(()->new StorehouseNotFoundException(aLong));
    }

    @Override
    public Storehouse save(Storehouse object) {
        return storehouseRepo.save(object);
    }

    @Override
    public void delete(Storehouse object) {
        storehouseRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        storehouseRepo.deleteById(aLong);
    }


    public Storehouse update(Storehouse newStorehouse, Long id){
        return storehouseRepo.findById(id).map(storehouse -> {

            storehouse.setName(newStorehouse.getName());
            storehouse.setAddress(newStorehouse.getAddress());
            storehouse.setMax_capacity(newStorehouse.getMax_capacity());

            return storehouseRepo.save(storehouse);
        }).orElseThrow(()->new StorehouseNotFoundException(id));
    }
}
