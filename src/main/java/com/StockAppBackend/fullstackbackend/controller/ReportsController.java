package com.StockAppBackend.fullstackbackend.controller;

import com.StockAppBackend.fullstackbackend.dto.*;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.service.map.DocumentInfoServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.ItemServiceImpl;
import com.StockAppBackend.fullstackbackend.service.map.ReportsService;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin("http://localhost:3000")
public class ReportsController {

    private final ReportsService reportsService;
    private final DocumentInfoServiceImpl documentInfoService;

    private final ItemServiceImpl itemService;

    public ReportsController(ReportsService reportsService, DocumentInfoServiceImpl documentInfoService, ItemServiceImpl itemService) {
        this.reportsService = reportsService;
        this.documentInfoService = documentInfoService;
        this.itemService = itemService;
    }


    @GetMapping("/report/{start}/{end}")
    List<Report> report(@PathVariable Date start,
                            @PathVariable Date end){

        return documentInfoService.findDocInfoForReports(start,end);
    }

    @GetMapping("/abcreport/{start}/{end}")
    List<AbcXyzItemDTO> abcAnalysis(@PathVariable Date start,
                                    @PathVariable Date end){

        return documentInfoService.AbcXyzAnalysis(start,end);
    }

    @GetMapping("/balance_report/{start}/{end}")
    List<BalanceReport> getBalanceReport(@PathVariable Date start,
                                         @PathVariable Date end){

        return documentInfoService.generateBalanceReport(start,end);
    }

    @PostMapping("/calculate")
    public List<Calculation> calculate(@RequestBody CalculationRequest request) {

        List<Long> selectedItems = request.getItems();
        List<Item> items = new ArrayList<>();
        for(Long id:selectedItems){
            items.add(itemService.findById(id));
        }

        return documentInfoService.calculate(items);
    }

    @PostMapping("/setcalculations")
    public void setCalculations(@RequestBody CalculationRequest request) {
        documentInfoService.setCalculations(request);
    }

}