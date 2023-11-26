package com.StockAppBackend.fullstackbackend.dto;

import com.StockAppBackend.fullstackbackend.entity.Item;

public class ForecastRequest {
    private Item item;
    private Double forecastAmount1;
    private Double forecastAmount2;
    private Double forecastAmount3;


    public ForecastRequest(){}

    public ForecastRequest(Item item, Double forecastAmount1, Double forecastAmount2, Double forecastAmount3) {
        this.item = item;
        this.forecastAmount1 = forecastAmount1;
        this.forecastAmount2 = forecastAmount2;
        this.forecastAmount3 = forecastAmount3;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getForecastAmount1() {
        return forecastAmount1;
    }

    public void setForecastAmount1(Double forecastAmount1) {
        this.forecastAmount1 = forecastAmount1;
    }

    public Double getForecastAmount2() {
        return forecastAmount2;
    }

    public void setForecastAmount2(Double forecastAmount2) {
        this.forecastAmount2 = forecastAmount2;
    }

    public Double getForecastAmount3() {
        return forecastAmount3;
    }

    public void setForecastAmount3(Double forecastAmount3) {
        this.forecastAmount3 = forecastAmount3;
    }
}
