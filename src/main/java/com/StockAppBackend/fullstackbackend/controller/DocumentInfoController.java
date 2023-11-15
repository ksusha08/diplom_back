package com.StockAppBackend.fullstackbackend.controller;


import com.StockAppBackend.fullstackbackend.entity.DocumentInfo;
import com.StockAppBackend.fullstackbackend.service.map.DocumentInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class DocumentInfoController {

    @Autowired
    private final DocumentInfoServiceImpl documentInfoService;


    public DocumentInfoController(DocumentInfoServiceImpl documentInfoService) {
        this.documentInfoService = documentInfoService;
    }


    @PostMapping("/documentInfo/{id}/{itemId}/")
    String newDocumentInfo(@RequestBody DocumentInfo newDocumentInfo, @PathVariable Long id, @PathVariable Long itemId){

        documentInfoService.saveDocInfo(newDocumentInfo,id,itemId);
        return  "DocumentInfo has been added success";
    }


    @GetMapping("/documentInfo")
    List<DocumentInfo> getAllDocuments(){

        return documentInfoService.findAll();
    }

    @GetMapping("/documentInfo/findByDocId/{id}")
    List<DocumentInfo> getDocumentByDocId(@PathVariable Long id){

        return documentInfoService.findByDocumentId(id);
    }

    @GetMapping("/documentInfo/{id}")
    DocumentInfo getDocumentById(@PathVariable Long id){

        return documentInfoService.findById(id);
    }

    @PutMapping("/documentInfo/{id}/{itemId}")
    DocumentInfo updateDocument(@RequestBody DocumentInfo newDocumentInfo, @PathVariable Long id, @PathVariable(required = false) Long itemId){

        return documentInfoService.update(newDocumentInfo,id,itemId);
    }


    @DeleteMapping("/documentInfo/{id}")
    String deleteDocument(@PathVariable Long id){

        documentInfoService.deleteById(id);
        return  "DocumentInfo with id "+id+" has been deleted success";

    }

}
