package com.StockAppBackend.fullstackbackend.entity;

public class TextRecommendation {
    private Item item;
    private String recommendationText;

    public TextRecommendation(Item item, String recommendationText) {
        this.item = item;
        this.recommendationText = recommendationText;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }
}