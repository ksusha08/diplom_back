package com.StockAppBackend.fullstackbackend.exception;

public class IncomeNotFoundException extends RuntimeException {
    public IncomeNotFoundException(Long id){
        super("Could not found the income with id "+ id);
    }
}
