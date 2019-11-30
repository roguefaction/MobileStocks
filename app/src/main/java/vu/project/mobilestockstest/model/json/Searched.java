package vu.project.mobilestockstest.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class Searched {

    @Getter
    @JsonProperty("1. symbol")
    private String symbol;

    @Getter
    @JsonProperty("2. name")
    private String name;

    @Getter
    @JsonProperty("3. type")
    private String type;

    @Getter
    @JsonProperty("4. region")
    private String region;

    @Getter
    @JsonProperty("5. marketOpen")
    private String marketOpen;

    @Getter
    @JsonProperty("6. marketClose")
    private String marketClose;

    @Getter
    @JsonProperty("7. timeZone")
    private String timezone;

    @Getter
    @JsonProperty("8. currency")
    private String currency;

    @Getter
    @JsonProperty("9. matchScore")
    private String matchScore;

}
