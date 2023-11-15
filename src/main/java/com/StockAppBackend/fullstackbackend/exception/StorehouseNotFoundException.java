package com.StockAppBackend.fullstackbackend.exception;

public class StorehouseNotFoundException extends RuntimeException {
    public StorehouseNotFoundException(Long id){
        super("Could not found the storehouse with id "+ id);
    }
}
