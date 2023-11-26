package com.StockAppBackend.fullstackbackend.service.map;



import com.StockAppBackend.fullstackbackend.dto.*;
import com.StockAppBackend.fullstackbackend.entity.*;
import com.StockAppBackend.fullstackbackend.exception.DocumentNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.DocumentInfoRepo;
import com.StockAppBackend.fullstackbackend.service.DocumentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentInfoServiceImpl implements DocumentInfoService {

    @Autowired
    private DocumentInfoRepo documentInfoRepo;

    @Autowired
    private final ItemServiceImpl itemService;

    @Autowired
    private final DocumentServiceImpl documentService;

    @Autowired
    private final StorehouseItemServiceImpl storehouseItemService;

    @Autowired
    private final StorehouseServiceImpl storehouseService;

    public DocumentInfoServiceImpl(ItemServiceImpl itemService, DocumentServiceImpl documentService, StorehouseItemServiceImpl storehouseItemService, StorehouseServiceImpl storehouseService) {
        this.itemService = itemService;
        this.documentService = documentService;
        this.storehouseItemService = storehouseItemService;
        this.storehouseService = storehouseService;
    }


    @Override
    public List<DocumentInfo> findAll() {
        List<DocumentInfo> documentInfo = documentInfoRepo.findAll();
        return documentInfo;
    }

    @Override
    public DocumentInfo findById(Long aLong) {
        return documentInfoRepo.findById(aLong).orElseThrow(()->new DocumentNotFoundException(aLong));
    }


    @Override
    public DocumentInfo save(DocumentInfo documentInfo) {

        return documentInfoRepo.save(documentInfo);
    }

    public void saveDocInfo(DocumentInfo documentInfo, Long idDoc, Long idItem) {

        Document doc = documentService.findById(idDoc);
        documentInfo.setDocument(doc);


        Item item = itemService.findById(idItem);
        documentInfo.setItem(item);

        StorehouseItem storehouseItem = storehouseItemService.findByItemAndStorehouse(item, doc.getStorehouse());

        if(storehouseItem==null){
            StorehouseItem newStorehouseItem = new StorehouseItem(item,doc.getStorehouse(),0,0,0,0,0,0);
            storehouseItemService.save(newStorehouseItem);
        }

        storehouseItem = storehouseItemService.findByItemAndStorehouse(item, doc.getStorehouse());

        Long id = item.getId();
        int newNumber = 0;
        int newAmount = 0;
        if(doc.getType().equals("приход")){
            newNumber = item.getNumber() + documentInfo.getAmount();
            newAmount=storehouseItem.getAmount()+documentInfo.getAmount();
        }else{
            newNumber = item.getNumber() - documentInfo.getAmount();
            newAmount=storehouseItem.getAmount()-documentInfo.getAmount();
        }
        storehouseItem.setAmount(newAmount);
        storehouseItemService.update(storehouseItem,storehouseItem.getId());

        item.setNumber(newNumber);
        itemService.update(item,id);

        documentInfoRepo.save(documentInfo);

        List<DocumentInfo> documentInfoList = findByDocumentId(doc.getId());
        documentService.updateSummAndAmount(doc, doc.getId(), documentInfoList);
    }

    @Override
    public void delete(DocumentInfo documentInfo) {

        Item item = documentInfo.getItem();
        Long id = item.getId();

        int newNumber = item.getNumber() + documentInfo.getAmount();
        item.setNumber(newNumber);

        itemService.update(item,id);

        documentInfoRepo.delete(documentInfo);
    }

    @Override
    public void deleteById(Long aLong) {

        DocumentInfo documentInfo = findById(aLong);
        Document doc = documentService.findById(documentInfo.getDocument().getId());


        Item item = documentInfo.getItem();
        Long id = item.getId();

        StorehouseItem storehouseItem = storehouseItemService.findByItemAndStorehouse(item, doc.getStorehouse());

        int newNumber = 0;
        int newAmount = 0;
        if(doc.getType().equals("приход")){
            newNumber = item.getNumber() - documentInfo.getAmount();
            newAmount=storehouseItem.getAmount()-documentInfo.getAmount();
        }else{
            newNumber = item.getNumber() + documentInfo.getAmount();
            newAmount=storehouseItem.getAmount()+documentInfo.getAmount();
        }
        item.setNumber(newNumber);
        itemService.update(item,id);


        documentInfoRepo.deleteById(aLong);
        List<DocumentInfo> newDocumentInfo = findByDocumentId(doc.getId());

        storehouseItem.setAmount(newAmount);
        storehouseItemService.update(storehouseItem,storehouseItem.getId());

        documentService.updateSummAndAmount(doc, doc.getId(),newDocumentInfo);
    }


    public DocumentInfo update(DocumentInfo newDocumentInfo, Long id, Long itemId ){

        return documentInfoRepo.findById(id).map(documentInfo -> {

            documentInfo.setAmount(newDocumentInfo.getAmount());
            documentInfo.setItem(newDocumentInfo.getItem());
            documentInfo.setSumm(newDocumentInfo.getSumm());

            if(itemId != null) {
                Item item = itemService.findById(itemId);
                documentInfo.setItem(item);
            }
            return documentInfoRepo.save(documentInfo);
        }).orElseThrow(()->new DocumentNotFoundException(id));
    }

    public List<DocumentInfo> findByDocumentId(Long documentId) {
        return documentInfoRepo.findByDocumentId(documentId);
    }

    public List<DocumentInfo> findByDocumentIdAndItemId(Long documentId,Long itemId) {
        return documentInfoRepo.findByDocumentIdAndItemId(documentId,itemId);
    }

    public List<Report> findDocInfoForReports(Date start, Date end) {
        List<Object[]> data = documentInfoRepo.findDocumentInfoForReports(start, end);
        List<Report> result = new ArrayList<>();

        for (Object[] obj : data) {
            Report info = new Report();
            info.setIdinfo((Long) obj[0]);
            info.setDocument((Document) obj[1]);
            info.setItem((Item) obj[2]);
            info.setAmount((Long) obj[3]);
            info.setSumm((Double) obj[4]);
            result.add(info);
        }

        return result;
    }

    public List<Forecast> findDocInfoForForecast(Date start, Date end, Item item) {
        List<Object[]> data = documentInfoRepo.findDocumentInfoForForecast(start, end, item);
        List<Forecast> result = new ArrayList<>();

        for (Object[] obj : data) {
            Forecast info = new Forecast();

            info.setItem((Item) obj[2]);
            System.out.println(info.getItem().getName());
            info.setMonth((String) obj[3]);
            System.out.println(info.getMonth());
            info.setAmount((Long) obj[4]);

            result.add(info);
        }

        return result;
    }

    public List<XyzDTO> findDocInfoXyzAnalysis(Date start, Date end) {

        List<Object[]> data = documentInfoRepo.findDocumentInfoForXyzAnalysis(start, end);
        List<XyzDTO> result = new ArrayList<>();

        for (Object[] obj : data) {
            XyzDTO info = new XyzDTO();
            info.setItem((Item) obj[0]);
            info.setMeanValue((Double) obj[1]);
            info.setStdDeviation((Double) obj[2]);
            result.add(info);
        }

        return result;
    }


    @Transactional(readOnly = false)
    public List<BalanceReport> generateBalanceReport(Date start, Date end) {
        List<Object[]> data = documentInfoRepo.report(start, end);
        List<BalanceReport> result = new ArrayList<>();

        for (Object[] obj : data) {
            BalanceReport info = new BalanceReport();


            info.setIdinfo((String) obj[0]);
            info.setIncomeAmount((BigDecimal) obj[1]);
            info.setExpenseAmount((BigDecimal) obj[2]);
            info.setNumber((int) obj[3]);

            result.add(info);
        }

        return result;
    }

    public void reUpdatePrices(Long docId){

        Document doc = documentService.findById(docId);
        Double newCoefficient = doc.getCoefficient();

        List<DocumentInfo> documentInfoList = findByDocumentId(docId);

        for(int i = 0; i<documentInfoList.size() ;i++) {

            DocumentInfo docInfoToChange = documentInfoList.get(i);

            documentInfoRepo.findById(docInfoToChange.getId()).map(documentInfo -> {

                Item item = docInfoToChange.getItem();
                Double discountItemPrice = item.getDiscountPrice();

                Double newCoefficientPrice = discountItemPrice*newCoefficient;

                documentInfo.setCoefficient_price(newCoefficientPrice);
                documentInfo.setSumm(docInfoToChange.getAmount()*newCoefficientPrice);


                return documentInfoRepo.save(documentInfo);
            });
        }
        documentInfoList = findByDocumentId(docId);

        documentService.updateSummAndAmount(doc,docId,documentInfoList);
    }



}
