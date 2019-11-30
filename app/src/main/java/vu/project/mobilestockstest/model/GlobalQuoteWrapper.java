package vu.project.mobilestockstest.model;

import com.google.gson.annotations.SerializedName;

public class GlobalQuoteWrapper {

    @SerializedName("Global Quote")
    private GlobalQuote globalQuote;

    public GlobalQuote getGlobalQuote() {
        return globalQuote;
    }
}
