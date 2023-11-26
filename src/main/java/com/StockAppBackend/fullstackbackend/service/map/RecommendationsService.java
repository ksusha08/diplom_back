package com.StockAppBackend.fullstackbackend.service.map;

import com.StockAppBackend.fullstackbackend.TemplateMethod.PdfStockRecommendationGenerator;

import com.StockAppBackend.fullstackbackend.TemplateMethod.StockRecommendationGenerator;
import com.StockAppBackend.fullstackbackend.TemplateMethod.WordStockRecommendationGenerator;
import com.StockAppBackend.fullstackbackend.analytics.HoltWintersForecast;
import com.StockAppBackend.fullstackbackend.dto.Forecast;
import com.StockAppBackend.fullstackbackend.dto.ForecastRequest;
import com.StockAppBackend.fullstackbackend.entity.Item;


import com.StockAppBackend.fullstackbackend.entity.TextRecommendation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public RecommendationsService(DocumentInfoServiceImpl documentInfoService, ItemServiceImpl itemService) {
        this.documentInfoService = documentInfoService;
        this.itemService = itemService;
    }

    public List<TextRecommendation> generateTextRecommendations(List<ForecastRequest> forecastRequests) {
        List<TextRecommendation> textRecommendations = new ArrayList<>();

        for (ForecastRequest forecastRequest : forecastRequests) {
            Item item = forecastRequest.getItem();
            double forecast1 = forecastRequest.getForecastAmount1();
            double forecast2 = forecastRequest.getForecastAmount2();
            double forecast3 = forecastRequest.getForecastAmount3();

            // Ваши логические условия для формирования текстовых рекомендаций
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

        if (forecast3 > forecast2 && forecast2 > forecast1) {
            return "Рекомендуется увеличить запас номенклатуры '" + item.getName() + "'.";
        } else if (forecast3 < forecast2 && forecast2 < forecast1) {
            return "Рекомендуется уменьшить запас номенклатуры '" + item.getName() + "'.";
        } else {
            return "Рекомендации для номенклатуры '" + item.getName() + "' не определены.";
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

    public List<ForecastRequest>  doForecastCalculations(List<Item> itemsList) throws ParseException {

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

        return result;
    }

    /*public  List<Forecast>  findInfoForForecast(Item item) throws ParseException {

        Date end = new Date();;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();

        return documentInfoService.findDocInfoForForecast(start,end,item);
    }

    public String  doForecastCalculations(List<Item> itemsList) throws ParseException {

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


        String filepath =  ;
        generatePdfTable(result, filepath);
        return filepath;
    }*/


    /*public void generatePdfTable(List<ForecastRequest> forecastRequests, String filePath) {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Используйте шрифт Arial для поддержки кириллицы
            BaseFont baseFont = BaseFont.createFont("D:/УНИВЕР/7-СЕМЕСТР/КУРСАЧ-ДИПЛОМ/Приложение/TimesNewRoman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont);

            // Создайте таблицу с 4 столбцами
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Добавьте заголовки таблицы
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

            // Добавьте данные в таблицу
            for (ForecastRequest forecastRequest : forecastRequests) {
                table.addCell(new PdfPCell(new Phrase(forecastRequest.getItem().getName(), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(forecastRequest.getItem().getNumber()), font)));


                table.addCell(new PdfPCell(new Phrase(String.valueOf(Math.round(forecastRequest.getForecastAmount1())), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(Math.round(forecastRequest.getForecastAmount2())), font)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(Math.round(forecastRequest.getForecastAmount3())), font)));
            }

            // Добавьте таблицу в документ
            document.add(table);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
*/

}
