package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.DocumentInfo;
import com.StockAppBackend.fullstackbackend.entity.Income;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.exception.IncomeNotFoundException;
import com.StockAppBackend.fullstackbackend.exception.ItemNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.IncomeRepo;
import com.StockAppBackend.fullstackbackend.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepo incomeRepo;

    @Autowired
    private final DocumentInfoServiceImpl documentInfoServiceImpl;

    @Autowired
    private final DocumentServiceImpl documentServiceImpl;

    @Autowired
    private final ItemServiceImpl itemServiceImpl;

    public IncomeServiceImpl(DocumentInfoServiceImpl documentInfoServiceImpl, DocumentServiceImpl documentServiceImpl, ItemServiceImpl itemServiceImpl) {
        this.documentInfoServiceImpl = documentInfoServiceImpl;
        this.documentServiceImpl = documentServiceImpl;
        this.itemServiceImpl = itemServiceImpl;
    }


    @Override
    public List<Income> findAll() {
        List<Income> income = incomeRepo.findAll();
        return income;
    }

    @Override
    public Income findById(Long aLong) {
        return incomeRepo.findById(aLong).orElseThrow(()->new IncomeNotFoundException(aLong));
    }


    @Override
    public Income save(Income object) {
        Document doc = object.getDocument();

        List<DocumentInfo> docInfo = documentInfoServiceImpl.findByDocumentId(doc.getId());

        double summ = 0;

        for(int i =0; i<docInfo.size();i++){
            summ = summ+docInfo.get(i).getSumm();
        }
        object.setSumm(summ);

        doc.setStatus("проведен");
        documentServiceImpl.save(doc);

        return incomeRepo.save(object);
    }

    public void addIncome( Long docId) {

        Document document = documentServiceImpl.findById(docId);

        List<DocumentInfo> docInfo = documentInfoServiceImpl.findByDocumentId(docId);

        for(int i =0; i<docInfo.size();i++){

            Income income = new Income();
            income.setDocument(document);

            Item item = itemServiceImpl.findById(docInfo.get(i).getItem().getId());
            income.setItem(item);

            income.setAmount(docInfo.get(i).getAmount());

            income.setCoefficient_price(docInfo.get(i).getCoefficient_price());

            income.setSumm(docInfo.get(i).getSumm());

            incomeRepo.save(income);
        }

        document.setStatus("проведен");
        documentServiceImpl.updateStatus(document,document.getId());

    }

    public void deleteIncome(Long docId) {

        Document document = documentServiceImpl.findById(docId);

        List<Income> incomeList = incomeRepo.findByDocumentId(docId);

        for(int i =0; i<incomeList.size();i++){

            Long incomeId = incomeList.get(i).getId();
            incomeRepo.deleteById(incomeId);

        }

        document.setStatus("не проведен");
        documentServiceImpl.updateStatus(document,document.getId());
    }

    @Override
    public void delete(Income object) {
        incomeRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        incomeRepo.deleteById(aLong);
    }


    public Income update(Income newIncome, Long id){

        return incomeRepo.findById(id).map(income -> {
            income.setSumm(newIncome.getSumm());

            return incomeRepo.save(income);
        }).orElseThrow(()->new ItemNotFoundException(id));
    }
}
