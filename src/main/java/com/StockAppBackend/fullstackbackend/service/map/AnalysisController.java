package com.StockAppBackend.fullstackbackend.service.map;

import com.StockAppBackend.fullstackbackend.dto.*;
import com.StockAppBackend.fullstackbackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisController {

    @Autowired
    private final DocumentInfoServiceImpl documentInfoService;

    @Autowired
    private final DocumentServiceImpl documentService;

    @Autowired
    private final ItemServiceImpl itemService;

    @Autowired
    private final StorehouseItemServiceImpl storehouseItemService;

    public AnalysisController(DocumentInfoServiceImpl documentInfoService, DocumentServiceImpl documentService, ItemServiceImpl itemService, StorehouseItemServiceImpl storehouseItemService) {
        this.documentInfoService = documentInfoService;
        this.documentService = documentService;
        this.itemService = itemService;
        this.storehouseItemService = storehouseItemService;
    }


    public List<Calculation> calculate(List<Item> items) {

        Date currentDate = new Date();

        List<Calculation> result = new ArrayList<>();
        List<Document> documentList = documentService.findAll();

        for (Item item : items) {
            Map<Storehouse, Map<Date, Double>> demandPerStorehouseAndDay = new HashMap<>();
            int maxDeliveryTime = 0;
            int minDeliveryTime = Integer.MAX_VALUE;

            for (Document doc : documentList) {
                if (doc.getStatus().equals("проведен") && doc.getDate() != null) {
                    Date docDate = doc.getDate();
                    Storehouse storehouse = doc.getStorehouse();

                    if (doc.getType().equals("расход")) {
                        List<DocumentInfo> documentInfoList = documentInfoService.findByDocumentIdAndItemId(doc.getId(), item.getId());
                        double docInfoAmount = 0;

                        for (DocumentInfo docInfo : documentInfoList) {
                            docInfoAmount += docInfo.getAmount();
                        }

                        demandPerStorehouseAndDay
                                .computeIfAbsent(storehouse, k -> new HashMap<>())
                                .put(docDate, demandPerStorehouseAndDay
                                        .getOrDefault(storehouse, new HashMap<>())
                                        .getOrDefault(docDate, 0.0) + docInfoAmount);
                    }

                    if (doc.getType().equals("приход")) {
                        if (doc.getDelivery() > maxDeliveryTime) {
                            maxDeliveryTime = doc.getDelivery();
                        }

                        if (doc.getDelivery() < minDeliveryTime) {
                            minDeliveryTime = doc.getDelivery();
                        }
                    }
                }
            }

            for (Map.Entry<Storehouse, Map<Date, Double>> entry : demandPerStorehouseAndDay.entrySet()) {
                Storehouse storehouse = entry.getKey();

                Map<Date, Double> demandPerDay = entry.getValue();
                double maxDemandPerDay = demandPerDay.values().stream().max(Comparator.naturalOrder()).orElse(0.0);

                // Расчет точки заказа по формуле RL = S_max × T_max
                int reorderLevel = (int) (maxDemandPerDay * maxDeliveryTime);

                double minDemandPerDay = demandPerDay.values().stream().min(Comparator.naturalOrder()).orElse(0.0);

                // Расчет минимального уровня запасов (I_min) по формуле I_min = RL - (S_min × T_min)
                int minLevel = (int) (reorderLevel - minDemandPerDay * minDeliveryTime);

                System.out.println(item.getName());
                System.out.println(storehouse.getName());
                
                double eoq = calculateEOQ(storehouseItemService.findByItemAndStorehouse(item,storehouse),currentDate);

                // Расчет максимального уровня запасов (I_max) по формуле I_max=RL+Q-(S_min×T_min)
                int maxLevel = (int) (reorderLevel+eoq-(minDemandPerDay * minDeliveryTime));
                System.out.println(eoq);

                Calculation calculation = new Calculation();
                calculation.setItem(item);
                calculation.setStorehouse(storehouse);
                calculation.setReorderLevel(reorderLevel);
                calculation.setMinValue(minLevel);
                calculation.setMaxValue(maxLevel);

                result.add(calculation);
            }
        }

        return result;
    }

    public void setCalculations(CalculationRequest request) {

        List<Long> selectedItems = request.getItems();

        List<Item> items = new ArrayList<>();
        for(Long id:selectedItems){
            items.add(itemService.findById(id));
        }

        List<Calculation> calculations = calculate(items);

        for(Calculation calculation:calculations){

            StorehouseItem storehouseItem = storehouseItemService.findByItemAndStorehouse(calculation.getItem(),calculation.getStorehouse());
            storehouseItem.setMin_amount(calculation.getMinValue());
            storehouseItem.setMax_amount(calculation.getMaxValue());
            storehouseItem.setReorder_level(calculation.getReorderLevel());
            storehouseItemService.update(storehouseItem,storehouseItem.getId());
        }

    }

    public List<AbcXyzItemDTO> AbcXyzAnalysis(Date start, Date end){

        List<XyzDTO> xyzResult = documentInfoService.findDocInfoXyzAnalysis(start, end);
        List<Report> info = documentInfoService.findDocInfoForReports(start, end);

        Map<Item, Double> itemSalesMap = new HashMap<>();

        for (Report report : info) {
            Item item = report.getItem();
            Double sales = report.getSumm();
            itemSalesMap.put(item, itemSalesMap.getOrDefault(item, 0.0) + sales);
        }

        List<Item> sortedItems = itemSalesMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<AbcXyzItemDTO> abcItems = new ArrayList<>();
        double totalSales = sortedItems.stream().mapToDouble(itemSalesMap::get).sum();
        double cumulativeSales = 0.0;
        for (Item item : sortedItems) {
            cumulativeSales += itemSalesMap.get(item);
            double salesPercentage = cumulativeSales / totalSales;

            String abcCategory;
            if (salesPercentage <= 0.7) {
                abcCategory = "A";
            } else if (salesPercentage <= 0.9) {
                abcCategory = "B";
            } else {
                abcCategory = "C";
            }

            // Определите XYZ-категорию с использованием функции determineXyzCategory
            String xyzCategory = determineXyzCategory(item, xyzResult);

            AbcXyzItemDTO abcItem = new AbcXyzItemDTO(item, abcCategory, xyzCategory, itemSalesMap.get(item));
            abcItems.add(abcItem);
        }

        return abcItems;
    }

    private String determineXyzCategory(Item item, List<XyzDTO> xyzResult) {
        // Найдите соответствующий объект XyzDTO для данного товара
        XyzDTO xyzItem = xyzResult.stream()
                .filter(dto -> dto.getItem().equals(item))
                .findFirst()
                .orElse(null);

        if (xyzItem != null) {
            double coefficientOfVariation = (xyzItem.getStdDeviation() / xyzItem.getMeanValue()) * 100;

            if (coefficientOfVariation < 10) {
                return "X";
            } else if (coefficientOfVariation >= 10 && coefficientOfVariation <= 25) {
                return "Y";
            } else {
                return "Z";
            }
        }

        // Если нет информации о XYZ, вернуть "X" по умолчанию
        return "X";
    }

    // Расчет оптимального размера заказа (Q) по формуле Q = √(2AS/I)
    public Double calculateEOQ (StorehouseItem storehouseItem, Date date){
        double eoq = 0;

        Date end = date;
        System.out.println(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();

        double yearSales = 0;
        double orderCost = storehouseItem.getOrder_cost();
        double maintenanceCost =storehouseItem.getMaintenance_cost();

        List<Report> info = documentInfoService.findDocInfoForReports(start, date);
        Item item = storehouseItem.getItem();

        for (Report report : info) {
            if(report.getItem().getId() == item.getId()) {
                Double sales = report.getSumm();
                yearSales += sales;
            }
        }
        System.out.println(yearSales);
        System.out.println(orderCost);
        System.out.println(maintenanceCost);

        eoq = Math.sqrt((2 * orderCost * yearSales) / maintenanceCost);


        return eoq;

    }


}
