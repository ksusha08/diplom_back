package com.StockAppBackend.fullstackbackend.service.map;

import com.StockAppBackend.fullstackbackend.TemplateMethod.PdfStockRecommendationGenerator;

import com.StockAppBackend.fullstackbackend.TemplateMethod.StockRecommendationGenerator;
import com.StockAppBackend.fullstackbackend.TemplateMethod.WordStockRecommendationGenerator;
import com.StockAppBackend.fullstackbackend.analytics.HoltWintersForecast;
import com.StockAppBackend.fullstackbackend.dto.AbcXyzItemDTO;
import com.StockAppBackend.fullstackbackend.dto.Forecast;
import com.StockAppBackend.fullstackbackend.dto.ForecastRequest;
import com.StockAppBackend.fullstackbackend.entity.Item;


import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.entity.StorehouseItem;
import com.StockAppBackend.fullstackbackend.entity.TextRecommendation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class RecommendationsService {
    @Autowired
    private final DocumentInfoServiceImpl documentInfoService;

    @Autowired
    private final ItemServiceImpl itemService;

    @Autowired
    private final StorehouseItemServiceImpl storehouseItemService;

    @Autowired
    private final AnalysisController analysisController;

    public RecommendationsService(DocumentInfoServiceImpl documentInfoService, ItemServiceImpl itemService, StorehouseItemServiceImpl storehouseItemService, AnalysisController analysisController) {
        this.documentInfoService = documentInfoService;
        this.itemService = itemService;
        this.storehouseItemService = storehouseItemService;
        this.analysisController = analysisController;
    }

    public List<TextRecommendation> generateTextRecommendations(List<ForecastRequest> forecastRequests) {
        List<TextRecommendation> textRecommendations = new ArrayList<>();


        for (ForecastRequest forecastRequest : forecastRequests) {
            Item item = forecastRequest.getItem();
            double forecast1 = forecastRequest.getForecastAmount1();
            double forecast2 = forecastRequest.getForecastAmount2();
            double forecast3 = forecastRequest.getForecastAmount3();


            String recommendationText = generateRecommendationText(item, forecast1, forecast2, forecast3);

            TextRecommendation textRecommendation = new TextRecommendation(item, recommendationText);
            textRecommendations.add(textRecommendation);
        }

        StockRecommendationGenerator pdfGenerator = new PdfStockRecommendationGenerator();
        pdfGenerator.generateRecommendation(textRecommendations);


        StockRecommendationGenerator wordGenerator = new WordStockRecommendationGenerator();
        wordGenerator.generateRecommendation(textRecommendations);


        return textRecommendations;
    }

    private String generateRecommendationText(Item item, double forecast1, double forecast2, double forecast3) {

        double currentStock = item.getNumber();
        String itemName = item.getName();

        List<StorehouseItem> list = storehouseItemService.findByItemId(item.getId());

        double allMinAmount = 0;


        for(StorehouseItem storehouseItem:list){
            allMinAmount += storehouseItem.getMin_amount();
        }

        if (currentStock > (forecast3+forecast2+forecast1)) {
            return String.format("Текущий остаток номенклатуры '%s' составляет "+item.getNumber()+" , что выше прогноза на 3 месяца. Резер запасов, исходя из установленных минимальных уровней данной номенклатурной позиции на складах, составляет "+allMinAmount+". Рекомендуется уменьшить запас на %d единиц.", itemName, (int) (currentStock - (forecast3+forecast2+forecast1+allMinAmount)));
        } else if (currentStock < (forecast3+forecast2+forecast1) ) {
            return String.format("Текущий остаток номенклатуры '%s' составляет "+item.getNumber()+" , что ниже  прогноза на 3 месяца. Резер запасов, исходя из установленных минимальных уровней данной номенклатурной позиции на складах, составляет "+allMinAmount+". Рекомендуется увеличить запас на %d единиц.", itemName, (int) ( (forecast3+forecast2+forecast1+allMinAmount) - currentStock));
        } else {
            return String.format("Текущий остаток номенклатуры '%s' находится в пределах прогнозов.", itemName);
        }
    }


    public List<TextRecommendation> generateABCXYZTextRecommendations(List<ForecastRequest> forecastRequests) {

        List<TextRecommendation> textRecommendations = new ArrayList<>();


        for (ForecastRequest forecastRequest : forecastRequests) {
            Item item = forecastRequest.getItem();


            String recommendationText = generateABCXYZRecommendationText(item);

            TextRecommendation textRecommendation = new TextRecommendation(item, recommendationText);
            textRecommendations.add(textRecommendation);
        }

        StockRecommendationGenerator pdfGenerator = new PdfStockRecommendationGenerator();
        pdfGenerator.generateRecommendation(textRecommendations);


        StockRecommendationGenerator wordGenerator = new WordStockRecommendationGenerator();
        wordGenerator.generateRecommendation(textRecommendations);


        return textRecommendations;
    }

    private String generateABCXYZRecommendationText(Item item) {
        String abcxyzCategory = findABCXYZCategory(item);
        String itemName = item.getName();

        switch (abcxyzCategory) {
            case "AX":
                return String.format("Номенклатура '%s' относится к категории AX. Данная номенклатурная позиция имеет высокие объемы расходов, стабильный спрос. Рекомендуется поддерживать постоянное наличие, а также делать упор на улучшение и развитие.", itemName);
            case "AY":
                return String.format("Номенклатура '%s' относится к категории AY. Данная номенклатурная позиция имеет высокие объемы расходов, нерегулярный спрос. Рекомендуется поддерживать постоянное наличие, делать упор на улучшение и развитие, а также увеличить страховой запас.", itemName);
            case "AZ":
                return String.format("Номенклатура '%s' относится к категории AZ. Данная номенклатурная позиция имеет высокие объемы расходов, единичный спрос. Рекомендуется пересмотреть систему заказов", itemName);
            case "BX":
                return String.format("Номенклатура '%s' относится к категории BX. Данная номенклатурная позиция имеет средние объемы расходов, стабильный спрос. Рекомендуется поддерживать постоянное наличие, а также делать упор на улучшение и развитие.", itemName);
            case "BY":
                return String.format("Номенклатура '%s' относится к категории BY. Данная номенклатурная позиция имеет средние объемы расходов, нерегулярный спрос.Рекомендуется поддерживать постоянное наличие, делать упор на улучшение и развитие, а также увеличить страховой запас", itemName);
            case "BZ":
                return String.format("Номенклатура '%s' относится к категории BZ. Данная номенклатурная позиция имеет средние объемы расходов, единичный спрос. Рекомендуется пересмотреть систему заказов", itemName);
            case "CX":
                return String.format("Номенклатура '%s' относится к категории CX. Данная номенклатурная позиция имеет низкие объемы расходов, стабильный спрос. Рекомендуется поддерживать постоянное наличие, а также делать упор на улучшение и развитие.", itemName);
            case "CY":
                return String.format("Номенклатура '%s' относится к категории CY. Данная номенклатурная позиция имеет низкие объемы расходов, нерегулярный спрос. Рекомендуется поддерживать запас с небольшим излишком", itemName);
            case "CZ":
                return String.format("Номенклатура '%s' относится к категории CZ. Данная номенклатурная позиция имеет низкие объемы расходов, единичный спрос. Рекомендуется регулярный контроль, а также стоит поставить вопрос о полном исключении позиции из ассортимента", itemName);
            default:
                return "Нет рекомендаций для данной категории.";
        }
    }

    public  List<Forecast>  findInfoForForecast(Item item) throws ParseException {

        Date end = new Date();;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();

        return documentInfoService.findDocInfoForForecast(start,end,item);
    }

    public String findABCXYZCategory(Item item){
        Date end = new Date();;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();

        List<AbcXyzItemDTO> items = analysisController.AbcXyzAnalysis(start,end);

        String category = "";
        for(AbcXyzItemDTO abcXyzItemDTO: items){
            if (abcXyzItemDTO.getItem().getId() == item.getId()){
                category = abcXyzItemDTO.getAbcCategory()+abcXyzItemDTO.getXyzCategory();
            }

        }

        return category;
    }

    public byte[]  doForecastCalculations(List<Item> itemsList) throws ParseException {

        List<ForecastRequest> result = new ArrayList<>();

        for(Item item:itemsList){
            List<Forecast> list = findInfoForForecast(item);

            HoltWintersForecast forecastModel = new HoltWintersForecast(0.2, 0.1, 0.3, 12);

            for(Forecast forecast: list){
                double data = forecast.getAmount();
                forecastModel.update(data);
            }

            // Прогноз на следующие 3 месяца
            int horizon = 3;

            double forecast1 = 0 ;
            double forecast2 = 0;
            double forecast3 = 0;

            for (int i = 1; i <= horizon; i++) {
                double forecast = forecastModel.forecast(i);

                if(i==1){
                    forecast1 = forecast;
                }
                if(i==2){
                    forecast2 = forecast;
                }
                if(i==3){
                    forecast3 = forecast;
                }

            }


            ForecastRequest forecastRequest = new ForecastRequest(item,forecast1,forecast2,forecast3);
            result.add(forecastRequest);

        }


        return generatePdf(result,generateTextRecommendations(result), generateABCXYZTextRecommendations(result));
    }

    public byte[] generatePdf(List<ForecastRequest> forecastRequests, List<TextRecommendation> textRecommendations, List<TextRecommendation> textABCXYZRecommendations) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            BaseFont baseFont = BaseFont.createFont("D:/УНИВЕР/7-СЕМЕСТР/КУРСАЧ-ДИПЛОМ/Приложение/TimesNewRoman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont,14);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell(new PdfPCell(new Phrase("Номенклатура", font)));
            table.addCell(new PdfPCell(new Phrase("Текущий остаток", font)));

            LocalDate currentDate = LocalDate.now();
            LocalDate nextMonth = currentDate.plusMonths(1);
            LocalDate nextMonth2 = currentDate.plusMonths(2);
            LocalDate nextMonth3 = currentDate.plusMonths(3);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("ru"));

            table.addCell(new PdfPCell(new Phrase("Прогноз для "+ nextMonth.format(formatter).toString()+" "+nextMonth.getYear(), font)));
            table.addCell(new PdfPCell(new Phrase("Прогноз для "+ nextMonth2.format(formatter).toString()+" "+nextMonth2.getYear(), font)));
            table.addCell(new PdfPCell(new Phrase("Прогноз для " + nextMonth3.format(formatter).toString()+" "+nextMonth3.getYear(), font)));

            for (ForecastRequest forecastRequest : forecastRequests) {
                table.addCell(new PdfPCell(new Phrase(forecastRequest.getItem().getName(), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(forecastRequest.getItem().getNumber()), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(Math.round(forecastRequest.getForecastAmount1())), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(Math.round(forecastRequest.getForecastAmount2())), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(Math.round(forecastRequest.getForecastAmount3())), font)));
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Рекомендации:", font));

            com.itextpdf.text.List orderedList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED);
            orderedList.setNumbered(true);

            for (TextRecommendation textRecommendation : textRecommendations) {

                Paragraph recommendationParagraph = new Paragraph(textRecommendation.getRecommendationText(), font);
                orderedList.add(new ListItem(recommendationParagraph));
            }
            document.add(orderedList);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Рекомендации по категорям ABC-XYZ:", font));

            com.itextpdf.text.List orderedList2 = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED);
            orderedList2.setNumbered(true);

            for (TextRecommendation textRecommendation : textABCXYZRecommendations) {

                Paragraph recommendationParagraph = new Paragraph(textRecommendation.getRecommendationText(), font);
                orderedList2.add(new ListItem(recommendationParagraph));
            }


            document.add(orderedList2);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }


}
