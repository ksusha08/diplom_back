package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.DocumentInfo;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.entity.Supplier;
import com.StockAppBackend.fullstackbackend.exception.DocumentNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.DocumentRepo;
import com.StockAppBackend.fullstackbackend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private final SupplierServiceImpl supplierService;

    @Autowired
    private final StorehouseServiceImpl storehouseService;


    public DocumentServiceImpl(SupplierServiceImpl supplierService, StorehouseServiceImpl storehouseService) {
        this.supplierService = supplierService;
        this.storehouseService = storehouseService;
    }

    @Override
    public List<Document> findAll() {
        List<Document> documents = documentRepo.findAll();
        return documents;
    }

    @Override
    public Document findById(Long aLong) {
        return documentRepo.findById(aLong).orElseThrow(()->new DocumentNotFoundException(aLong));
    }


    @Override
    public Document save(Document object) {

        object.setAmount(0);
        object.setSumm(0.0);

        return documentRepo.save(object);
    }

    @Override
    public void delete(Document object) {
        documentRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        documentRepo.deleteById(aLong);
    }


    public Document update(Document newDocument, Long id, Long supplierId, Long storehouseId ){

        return documentRepo.findById(id).map(document -> {
            document.setNumber(newDocument.getNumber());
            document.setDate(newDocument.getDate());
            document.setType(newDocument.getType());
            document.setSumm(newDocument.getSumm());
            document.setAmount(newDocument.getAmount());
            document.setCoefficient(newDocument.getCoefficient());
            document.setDelivery(newDocument.getDelivery());

            if(supplierId != null) {
                Supplier supplier = supplierService.findById(supplierId);
                document.setSupplier(supplier);
            }
            if(storehouseId != null) {
                Storehouse storehouse = storehouseService.findById(storehouseId);
                document.setStorehouse(storehouse);
            }
            return documentRepo.save(document);

        }).orElseThrow(()->new DocumentNotFoundException(id));
    }


    public Document updateStatus(Document newDocument, Long id){

        return documentRepo.findById(id).map(document -> {

            document.setStatus(newDocument.getStatus());


            return documentRepo.save(document);
        }).orElseThrow(()->new DocumentNotFoundException(id));
    }

    public Document updateSummAndAmount(Document newDocument, Long id, List<DocumentInfo> docInfo){

        double summ = 0;
        int amount = 0;

        for(int i = 0; i<docInfo.size();i++){
            amount = amount+docInfo.get(i).getAmount();
            summ = summ + docInfo.get(i).getSumm();
        }

        newDocument.setAmount(amount);
        newDocument.setSumm(summ);

        return documentRepo.findById(id).map(document -> {

            document.setAmount(newDocument.getAmount());
            document.setSumm(newDocument.getSumm());


            return documentRepo.save(document);
        }).orElseThrow(()->new DocumentNotFoundException(id));
    }

    public List<Document> findByNumber(String number) {
        List<Document> documents = documentRepo.findByNumberContainingIgnoreCase(number);
        return documents;
    }

    public List<Document> findByDateBetween(Date start, Date end) {
        List<Document> documents = documentRepo.findByDateGreaterThanEqualAndDateLessThanEqual(start,end);
        return documents;
    }
}
