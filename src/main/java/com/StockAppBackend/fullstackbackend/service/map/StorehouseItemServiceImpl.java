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

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private StorehouseServiceImpl storehouseService;



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

    public void moveItem(Long idstorehouse1,Long iditem,Long idstorehouse2, int amount){

        Item item = itemService.findById(iditem);

        StorehouseItem storehouseItem1 = findByItemAndStorehouse(item,storehouseService.findById(idstorehouse1));

        StorehouseItem storehouseItem2 = findByItemAndStorehouse(item,storehouseService.findById(idstorehouse2));

        int newAmount1 = storehouseItem1.getAmount() - amount;
        storehouseItem1.setAmount(newAmount1);
        update(storehouseItem1,idstorehouse1);

        if(storehouseItem2==null){
            StorehouseItem newStorehouseItem = new StorehouseItem(item,storehouseService.findById(idstorehouse2),amount,0,0,0,0,0);
            save(newStorehouseItem);
        }
        else{
            int newAmount2 = storehouseItem2.getAmount() +amount;
            storehouseItem2.setAmount(newAmount2);
            update(storehouseItem2,idstorehouse2);
        }


    }

}
