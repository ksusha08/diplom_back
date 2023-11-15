package com.StockAppBackend.fullstackbackend.controller;


import com.StockAppBackend.fullstackbackend.entity.Expense;
import com.StockAppBackend.fullstackbackend.service.map.DocumentServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.ExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class ExpenseController {

    @Autowired
    private final ExpenseServiceImpl expenseService;

    @Autowired
    private final DocumentServiceImpl documentService;

    public ExpenseController(ExpenseServiceImpl expenseService, DocumentServiceImpl documentService) {
        this.expenseService = expenseService;
        this.documentService = documentService;
    }


    @PostMapping("/expense/{id}/")
    void newExpense( @PathVariable Long id){

        expenseService.addExpense(id);
    }



    @GetMapping("/expense")
    List<Expense> getAllExpenses(){
        return expenseService.findAll();
    }


    @GetMapping("/expense/{id}")
    Expense getExpenseById(@PathVariable Long id){
        return expenseService.findById(id);
    }

    @PutMapping("/expense/{id}")
    Expense updateExpense(@RequestBody Expense newExpense, @PathVariable Long id){

        return expenseService.update(newExpense,id);
    }


    @DeleteMapping("/expense/{id}")
    String deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return  "DocumentInfo with id "+id+" has been deleted success";

    }
}
