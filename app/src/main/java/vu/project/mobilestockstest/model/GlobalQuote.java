package vu.project.mobilestockstest.model;

import com.google.gson.annotations.SerializedName;

public class GlobalQuote {

    @SerializedName("01. symbol")
    private String symbol;

    @SerializedName("02. open")
    private String open;

    @SerializedName("03. high")
    private String high;

    @SerializedName("04. low")
    private String low;

    @SerializedName("05. price")
    private String price;

    @SerializedName("06. volume")
    private String volume;

    @SerializedName("07. latest trading day")
    private String latestTradingDay;

    @SerializedName("08. previous close")
    private String previousClose;

    @SerializedName("09. change")
    private String change;

    @SerializedName("10. change percent")
    private String changePercent;

    public String getVolume() {
        return volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getOpen() {
        return open;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getPrice() {
        return price;
    }

    public String getLatestTradingDay() {
        return latestTradingDay;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public String getChange() {
        return change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
