package vu.project.mobilestockstest.model;

public class HistoricalStockData {

    private String date;

    private StockData data;

    public HistoricalStockData(String date, StockData data) {
        this.date = date;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public StockData getData() {
        return data;
    }
}
