package vu.project.mobilestockstest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class SearchedStockItem implements Serializable {

    @SerializedName("1. symbol")
    private String symbol;

    @SerializedName("2. name")
    private String name;

    @SerializedName("3. type")
    private String type;

    @SerializedName("4. region")
    private String region;

    @SerializedName("5. marketOpen")
    private String marketOpen;

    @SerializedName("6. marketClose")
    private String marketClose;

    @SerializedName("7. timeZone")
    private String timezone;

    @SerializedName("8. currency")
    private String currency;

    @SerializedName("9. matchScore")
    private String matchScore;

    @NotNull
    @Override
    public String toString() {
        return this.symbol + ": " + this.name;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public String getRegion() {
        return region;
    }

    public String getMarketOpen() {
        return marketOpen;
    }

    public String getMarketClose() {
        return marketClose;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMatchScore() {
        return matchScore;
    }
}
