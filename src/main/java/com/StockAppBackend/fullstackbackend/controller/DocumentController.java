package com.StockAppBackend.fullstackbackend.controller;



import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.entity.Supplier;
import com.StockAppBackend.fullstackbackend.entity.User;
import com.StockAppBackend.fullstackbackend.service.map.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class DocumentController {

    @Autowired
    private final DocumentServiceImpl documentService;

    @Autowired
    private final DocumentInfoServiceImpl documentInfoService;

    @Autowired
    private final UserServiceImpl userService;

    @Autowired
    private final SupplierServiceImpl supplierService;

    @Autowired
    private final StorehouseServiceImpl storehouseService;

    public DocumentController(DocumentServiceImpl documentService, DocumentInfoServiceImpl documentInfoService, UserServiceImpl userService, SupplierServiceImpl supplierService, StorehouseServiceImpl storehouseService) {

        this.documentService = documentService;
        this.documentInfoService = documentInfoService;
        this.userService = userService;
        this.supplierService = supplierService;
        this.storehouseService = storehouseService;
    }


    @PostMapping("/document/{userId}/{supplierId}/{storehouseId}")
    Document newDocument(@RequestBody Document newDocument, @PathVariable Long userId, @PathVariable Long supplierId, @PathVariable Long storehouseId){

        Supplier supplier = supplierService.findById(supplierId);
        newDocument.setSupplier(supplier);

        User user = userService.findById(userId);
        newDocument.setUser(user);

        Storehouse storehouse = storehouseService.findById(storehouseId);
        newDocument.setStorehouse(storehouse);


        return documentService.save(newDocument);
    }

    @GetMapping("/documents")
    List<Document> getAllDocuments(){
        return documentService.findAll();
    }

    @GetMapping("/document/{id}")
    Document getDocumentById(@PathVariable Long id){
        return documentService.findById(id);
    }

    @GetMapping("/search_document/{number}")
    List<Document> getDocumentsByNumber(@PathVariable String number){

        return documentService.findByNumber(number);
    }

    @GetMapping("/filter_document/{start}/{end}")
    List<Document> getDocumentsByDate(@PathVariable Date start,
                                      @PathVariable Date end){

        return documentService.findByDateBetween(start,end);
    }

    @PutMapping("/document/{id}/{supplierId}/{storehouseId}")
    Document updateDocument(@RequestBody Document newDocument, @PathVariable Long id, @PathVariable(required = false) Long supplierId, @PathVariable(required = false) Long storehouseId){

        Document updatedDocument = documentService.update(newDocument,id,supplierId,storehouseId);
        documentInfoService.reUpdatePrices(id);

        return updatedDocument;
    }


    @DeleteMapping("/document/{id}")
    String deleteDocument(@PathVariable Long id){
        documentService.deleteById(id);
        return  "Document with id "+id+" has been deleted success";

    }
}


