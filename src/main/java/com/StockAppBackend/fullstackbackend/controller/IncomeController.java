package com.StockAppBackend.fullstackbackend.controller;


import com.StockAppBackend.fullstackbackend.entity.Income;
import com.StockAppBackend.fullstackbackend.service.map.DocumentServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.IncomeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class IncomeController {

    @Autowired
    private final IncomeServiceImpl incomeService;

    @Autowired
    private final DocumentServiceImpl documentService;

    public IncomeController(IncomeServiceImpl incomeService, DocumentServiceImpl documentService) {
        this.incomeService = incomeService;
        this.documentService = documentService;
    }




    @PostMapping("/income/{id}/")
    String newIncome(@PathVariable Long id){

        incomeService.addIncome(id);
        return  "Income for document with id "+id+" has been added";
    }



    @GetMapping("/income")
    List<Income> getAllDocuments(){
        return incomeService.findAll();
    }


    @GetMapping("/income/{id}")
    Income getIncomeById(@PathVariable Long id){
        return incomeService.findById(id);
    }

    @PutMapping("/income/{id}")
    Income updateIncome(@RequestBody Income newIncome, @PathVariable Long id){

        return incomeService.update(newIncome,id);
    }


    @DeleteMapping("/income/{id}")
    String deleteDocument(@PathVariable Long id){
        incomeService.deleteIncome(id);
        return  "DocumentInfo with id "+id+" has been deleted success";

    }
}
