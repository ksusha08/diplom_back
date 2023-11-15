package com.StockAppBackend.fullstackbackend.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id){
        super("Could not found the document with id "+ id);
    }
}
