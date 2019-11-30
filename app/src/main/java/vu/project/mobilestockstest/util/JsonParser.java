package vu.project.mobilestockstest.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import vu.project.mobilestockstest.model.json.Data;

public class JsonParser {

    private ObjectMapper mapper;


    public JsonParser() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Data parseJson(String json) throws IOException {
        System.out.println("Parsing Json Response");
        return mapper.readValue(json, Data.class);
    }


}
