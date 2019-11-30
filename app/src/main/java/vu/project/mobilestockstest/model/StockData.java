package vu.project.mobilestockstest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class StockData {

    @SerializedName("1. open")
    private double open;

    @SerializedName("2. high")
    private double high;

    @SerializedName("3. low")
    private double low;

    @SerializedName("4. close")
    private double close;

    @SerializedName("5. volume")
    private double volume;

    @NotNull
    @Override
    public String toString() {
        return "open: " + this.open + ". high: " + this.high + ". low: " + this.low + ". close: " + this.close + ". volume: " + this.volume;
    }


    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }
}
