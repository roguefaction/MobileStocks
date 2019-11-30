package vu.project.mobilestockstest.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;


public class CurrencyConverter {

    private JsonObject currencyRateJson;

    public CurrencyConverter(JsonObject currencyRateJson) {
        this.currencyRateJson = currencyRateJson;
    }

    public Double convertAmountToCurrency(double amount, String desiredCurrencyCode) {
        double conversionRate = 0;

        for(Map.Entry<String, JsonElement> currencyRate: currencyRateJson.getAsJsonObject("rates").entrySet()){
            if(currencyRate.getKey().equals(desiredCurrencyCode)){
                conversionRate = currencyRate.getValue().getAsDouble();
            }

        }

        return amount * conversionRate;
    }

}
