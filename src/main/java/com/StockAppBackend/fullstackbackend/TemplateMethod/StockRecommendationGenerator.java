package com.StockAppBackend.fullstackbackend.TemplateMethod;

import com.StockAppBackend.fullstackbackend.entity.TextRecommendation;

import java.util.List;

public abstract class StockRecommendationGenerator {

    public void generateRecommendation( List<TextRecommendation> textRecommendation) {
        generateHeader();
        generateContent(textRecommendation);
        generateFooter();
    }

    private void generateHeader() {
    }

    protected abstract void generateContent( List<TextRecommendation> textRecommendation);

    protected abstract void generateFooter();
}
