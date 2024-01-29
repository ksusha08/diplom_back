package com.StockAppBackend.fullstackbackend.controller;

import com.StockAppBackend.fullstackbackend.dto.*;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.entity.TextRecommendation;
import com.StockAppBackend.fullstackbackend.service.map.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin("http://localhost:3000")
public class ReportsController {

    private final ReportsService reportsService;
    private final DocumentInfoServiceImpl documentInfoService;

    private final ItemServiceImpl itemService;

    private final AnalysisController analysisController;

    private final RecommendationsService recommendationsService;


    public ReportsController(ReportsService reportsService, DocumentInfoServiceImpl documentInfoService, ItemServiceImpl itemService, AnalysisController analysisController, RecommendationsService recommendationsService) {
        this.reportsService = reportsService;
        this.documentInfoService = documentInfoService;
        this.itemService = itemService;
        this.analysisController = analysisController;
        this.recommendationsService = recommendationsService;
    }


    @GetMapping("/report/{start}/{end}")
    List<Report> report(@PathVariable Date start,
                            @PathVariable Date end){

        return documentInfoService.findDocInfoForReports(start,end);
    }

    @GetMapping("/abcreport/{start}/{end}")
    List<AbcXyzItemDTO> abcAnalysis(@PathVariable Date start,
                                    @PathVariable Date end){

        return analysisController.AbcXyzAnalysis(start,end);
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

        return analysisController.calculate(items);
    }

    @PostMapping("/setcalculations")
    public void setCalculations(@RequestBody CalculationRequest request) {
        analysisController.setCalculations(request);
    }



    @PostMapping("/recommendations")
    public ResponseEntity<byte[]> recommendations(@RequestBody CalculationRequest request) throws ParseException, IOException {

        List<Long> selectedItems = request.getItems();
        List<Item> items = new ArrayList<>();
        for(Long id:selectedItems){
            items.add(itemService.findById(id));
        }


        byte[] documentContent =  recommendationsService.doForecastCalculations(items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(documentContent, headers, HttpStatus.OK);
    }

}