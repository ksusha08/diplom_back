package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.DocumentInfo;
import com.StockAppBackend.fullstackbackend.entity.Expense;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.exception.IncomeNotFoundException;
import com.StockAppBackend.fullstackbackend.exception.ItemNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.ExpenseRepo;
import com.StockAppBackend.fullstackbackend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private final DocumentInfoServiceImpl documentInfoServiceImpl;

    @Autowired
    private final DocumentServiceImpl documentServiceImpl;

    @Autowired
    private final ItemServiceImpl itemServiceImpl;

    public ExpenseServiceImpl(DocumentInfoServiceImpl documentInfoServiceImpl, DocumentServiceImpl documentServiceImpl, ItemServiceImpl itemServiceImpl) {
        this.documentInfoServiceImpl = documentInfoServiceImpl;
        this.documentServiceImpl = documentServiceImpl;
        this.itemServiceImpl = itemServiceImpl;
    }


    @Override
    public List<Expense> findAll() {
        List<Expense> expense = expenseRepo.findAll();
        return expense;
    }

    @Override
    public Expense findById(Long aLong) {
        return expenseRepo.findById(aLong).orElseThrow(()->new IncomeNotFoundException(aLong));
    }

    @Override
    public Expense save(Expense object) {
        return null;
    }


    public void addExpense( Long docId) {

        Document document = documentServiceImpl.findById(docId);

        List<DocumentInfo> docInfo = documentInfoServiceImpl.findByDocumentId(docId);

        for(int i =0; i<docInfo.size();i++){

            Expense expense = new Expense();
            expense.setDocument(document);

            Item item = itemServiceImpl.findById(docInfo.get(i).getItem().getId());
            expense.setItem(item);

            expense.setAmount(docInfo.get(i).getAmount());

            expense.setCoefficient_price(docInfo.get(i).getCoefficient_price());

            expense.setSumm(docInfo.get(i).getSumm());

            expenseRepo.save(expense);
        }

        document.setStatus("проведен");
        documentServiceImpl.updateStatus(document,document.getId());

    }

    public void deleteExpense(Long docId) {

        Document document = documentServiceImpl.findById(docId);

        List<Expense> expenseList = expenseRepo.findByDocumentId(docId);

        for(int i =0; i<expenseList.size();i++){

            Long expenseId = expenseList.get(i).getId();
            expenseRepo.deleteById(expenseId);

        }

        document.setStatus("не проведен");
        documentServiceImpl.updateStatus(document,document.getId());
    }

    @Override
    public void delete(Expense object) {
        expenseRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        expenseRepo.deleteById(aLong);
    }


    public Expense update(Expense newExpense, Long id){

        return expenseRepo.findById(id).map(expense -> {
            expense.setSumm(newExpense.getSumm());

            return expenseRepo.save(expense);
        }).orElseThrow(()->new ItemNotFoundException(id));
    }
}
