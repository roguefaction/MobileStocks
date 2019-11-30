package vu.project.mobilestockstest.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

class Meta {

    @Getter
    @JsonProperty("1. Information")
    private String information;

    @Getter
    @JsonProperty("2. Symbol")
    private String symbol;

    @Getter
    @JsonProperty("3. Last Refreshed")
    private String lastRefreshed;

    @Getter
    @JsonProperty("4. Output Size")
    private String outputSize;

    @Getter
    @JsonProperty("4. Interval")
    private String interval;

    @Getter
    @JsonProperty("5. Time Zone")
    private String timeZone;

}
