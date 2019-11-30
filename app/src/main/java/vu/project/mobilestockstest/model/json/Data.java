package vu.project.mobilestockstest.model.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

public class Data {

    @Getter
    @JsonProperty(value = "Meta Data")
    private Meta meta;

    @Getter
    @JsonAlias({"Weekly Time Series",
            "Monthly Time Series",
            "Time Series (Daily)",
            "Time Series (5min)",
            "Time Series (10min)",
            "Time Series (15min)",
            "Time Series (30min)",
            "Time Series (60min)"})
    private LinkedHashMap<String, Metrics> timeSeriesData;

    @Getter
    @JsonProperty(value = "Note")
    private Meta note;

    @Getter
    @JsonProperty(value = "bestMatches")
    private List<Searched> searchResults;

}

