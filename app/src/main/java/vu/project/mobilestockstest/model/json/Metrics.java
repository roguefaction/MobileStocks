package vu.project.mobilestockstest.model.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class Metrics {

    @Getter
    @JsonProperty("1. open")
    private double open;

    @Getter
    @JsonProperty("2. high")
    private double high;

    @Getter
    @JsonProperty("3. low")
    private double low;

    @Getter
    @JsonProperty("4. close")
    private double close;

    @Getter
    @JsonProperty("5. adjusted close")
    private double adjustedClose;

    @Getter
    @JsonAlias({"5. volume", "6. volume"})
    private double volume;

    @Getter
    @JsonProperty("7. dividend amount")
    private double dividendAmount;

    @Getter
    @JsonProperty("8. split coefficient")
    private double splitCoefficient;

}
