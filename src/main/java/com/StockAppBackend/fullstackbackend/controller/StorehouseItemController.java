package com.StockAppBackend.fullstackbackend.controller;

import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.entity.StorehouseItem;
import com.StockAppBackend.fullstackbackend.service.map.DocumentServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.ItemServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.StorehouseItemServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.StorehouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class StorehouseItemController {

    private final StorehouseItemServiceImpl storehouseItemService;

    @Autowired
    private final StorehouseServiceImpl storehouseService;

    @Autowired
    private final ItemServiceImpl itemService;

    @Autowired
    private final DocumentServiceImpl docService;

    public StorehouseItemController(StorehouseItemServiceImpl storehouseItemService, StorehouseServiceImpl storehouseService, ItemServiceImpl itemService, DocumentServiceImpl docService) {
        this.storehouseItemService = storehouseItemService;
        this.storehouseService = storehouseService;
        this.itemService = itemService;
        this.docService = docService;
    }


    @PostMapping("/storehouseitem")
    StorehouseItem newStorehouse(@RequestBody StorehouseItem newStorehouseItem){
        return storehouseItemService.save(newStorehouseItem);
    }

    @GetMapping("/storehouseitems")
    List<StorehouseItem> getAllStorehouses(){
        return storehouseItemService.findAll();
    }

    @GetMapping("/storehouseitemsbyid/{id}")
    List<StorehouseItem> getAllStorehousesItemById(@PathVariable Long id){
        return storehouseItemService.findByItemId(id);
    }

    @GetMapping("/storehouseitem/{id}")
    StorehouseItem getStorehouseById(@PathVariable Long id){
        return storehouseItemService.findById(id);
    }

    @GetMapping("/storehouseitembystorageanditem/{iditem}/{iddoc}")
    StorehouseItem getStorehouseItemByStorageAndItem(@PathVariable Long iditem,@PathVariable Long iddoc){
        Item item = itemService.findById(iditem);
        Document doc = docService.findById(iddoc);

        return storehouseItemService.findByItemAndStorehouse(item,doc.getStorehouse());
    }

    @PutMapping("/storehouseitem/{id}")
    StorehouseItem updateStorehouse(@RequestBody StorehouseItem newStorehouseItem, @PathVariable Long id){
        return storehouseItemService.update(newStorehouseItem,id);
    }

    @DeleteMapping("/storehouseitem/{id}")
    String deleteStorehouseItem(@PathVariable Long id){
        storehouseItemService.deleteById(id);
        return  "StorehouseItem with id "+id+" has been deleted success";

    }

    @GetMapping("/move-storehouseitem/{idstorehouse1}/{iditem}/{idstorehouse2}/{amount}")
    String moveStorehouseItem(@PathVariable Long idstorehouse1,@PathVariable Long iditem, @PathVariable Long idstorehouse2, @PathVariable int amount){

        storehouseItemService.moveItem(idstorehouse1,iditem,idstorehouse2,amount);
        return  "StorehouseItem with id "+iditem+" has been moved success";

    }
}
