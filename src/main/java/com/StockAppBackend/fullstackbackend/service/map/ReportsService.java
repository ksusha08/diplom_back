package com.StockAppBackend.fullstackbackend.service.map;


import com.StockAppBackend.fullstackbackend.dto.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportsService {

    @Autowired
    private final ItemServiceImpl itemService;

    @Autowired
    private final DocumentServiceImpl documentService;

    @Autowired
    private final DocumentInfoServiceImpl documentInfoService;


    public ReportsService(ItemServiceImpl itemService, DocumentServiceImpl documentService, DocumentInfoServiceImpl documentInfoService) {
        this.itemService = itemService;
        this.documentService = documentService;
        this.documentInfoService = documentInfoService;
    }

    public List<Report> generate(Date start, Date end) {
/*
        List<Report> report = new ArrayList<>();

        List<Document> documents = documentService.findByDateBetween(start,end);

        for(int i = 0; i<documents.size(); i++){
            if(documents.get(i).getStatus().equals("проведен")){
               List<DocumentInfo> docInfo = documentInfoService.findByDocumentId(documents.get(i).getId());

                for(int n = 0; n<docInfo.size(); n++){
                    Report rep = new Report( docInfo.get(i).getItem().getName(), docInfo.get(i).getAmount(),docInfo.get(i).getSumm() );
                    report.add(rep);
                }

            }
        }*/

        List<Report> docInfo = documentInfoService.findDocInfoForReports(start,end);

        return docInfo;
    }
}
