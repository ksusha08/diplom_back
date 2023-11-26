package com.StockAppBackend.fullstackbackend.analytics;

public class HoltWintersForecast {
    private double level = 0;
    private double trend = 0;
    private double[] seasonal;
    private double alpha; // Коэффициент сглаживания уровня
    private double beta;  // Коэффициент сглаживания тренда
    private double gamma; // Коэффициент сглаживания сезонности
    private int seasonality; // Длина сезонного цикла

    public HoltWintersForecast(double alpha, double beta, double gamma, int seasonality) {
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.seasonality = seasonality;
        this.seasonal = new double[seasonality];
    }

    // Обновление уровня, тренда и сезонности
    public void update(double observation) {
        double lastLevel = level;
        double lastTrend = trend;

        // Обновление уровня
        level = alpha * (observation - seasonal[0]) + (1 - alpha) * (lastLevel + lastTrend);

        // Обновление тренда
        trend = beta * (level - lastLevel) + (1 - beta) * lastTrend;

        // Обновление сезонности
        for (int i = 0; i < seasonality; i++) {
            seasonal[i] = gamma * (observation - level) + (1 - gamma) * seasonal[i];
        }

        // Сдвиг сезонности
        shiftSeasonal();
    }

    // Прогнозирование на h периодов вперед
    public double forecast(int h) {
        double forecast = level + h * trend + seasonal[h % seasonality];
        return forecast;
    }

    // Сдвиг сезонности на один период
    private void shiftSeasonal() {
        for (int i = seasonality - 1; i > 0; i--) {
            seasonal[i] = seasonal[i - 1];
        }
        seasonal[0] = 0; // Новое значение сезонности
    }
}
