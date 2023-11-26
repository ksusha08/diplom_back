package com.StockAppBackend.fullstackbackend.service.map;

import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.entity.StorehouseItem;
import com.StockAppBackend.fullstackbackend.exception.StorehouseNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.StorehouseItemRepo;
import com.StockAppBackend.fullstackbackend.repo.StorehouseRepo;
import com.StockAppBackend.fullstackbackend.service.StorehouseItemService;
import com.StockAppBackend.fullstackbackend.service.StorehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorehouseItemServiceImpl implements StorehouseItemService {
    @Autowired
    private StorehouseItemRepo storehouseItemRepo;

    @Override
    public List<StorehouseItem> findAll() {
        List<StorehouseItem> storehousesItems = storehouseItemRepo.findAll();
        return storehousesItems;
    }

    @Override
    public StorehouseItem findById(Long aLong) {
        return storehouseItemRepo.findById(aLong).orElseThrow(()->new StorehouseNotFoundException(aLong));
    }

    @Override
    public StorehouseItem save(StorehouseItem object) {
        return storehouseItemRepo.save(object);
    }

    @Override
    public void delete(StorehouseItem object) {
        storehouseItemRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        storehouseItemRepo.deleteById(aLong);
    }


    public StorehouseItem update(StorehouseItem newStorehouseItem, Long id){
        return storehouseItemRepo.findById(id).map(storehouseItem -> {

            storehouseItem.setItem(newStorehouseItem.getItem());
            storehouseItem.setStorehouse(newStorehouseItem.getStorehouse());
            storehouseItem.setAmount(newStorehouseItem.getAmount());
            storehouseItem.setReorder_level(newStorehouseItem.getReorder_level());
            storehouseItem.setMin_amount(newStorehouseItem.getMin_amount());
            storehouseItem.setMax_amount(newStorehouseItem.getMax_amount());
            storehouseItem.setOrder_cost(newStorehouseItem.getOrder_cost());
            storehouseItem.setMaintenance_cost(newStorehouseItem.getMaintenance_cost());


            return storehouseItemRepo.save(storehouseItem);
        }).orElseThrow(()->new StorehouseNotFoundException(id));
    }

    public StorehouseItem findByItemAndStorehouse(Item item, Storehouse storehouse) {
        StorehouseItem storehouseItem = storehouseItemRepo.findByItemAndStorehouse(item, storehouse);
        return storehouseItem;
    }

    public List<StorehouseItem> findByItemId(Long id) {
        List<StorehouseItem> storehouseItems = storehouseItemRepo.findByItemId(id);
        return storehouseItems;
    }
}
