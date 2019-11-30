package vu.project.mobilestockstest.validator;

import com.google.gson.Gson;

import org.json.JSONObject;

import vu.project.mobilestockstest.model.ExpiredJson;

public class JsonValidator {

    public static boolean wasJsonCallLimitReached(JSONObject json) {

        Gson gson = new Gson();
        ExpiredJson expiredJson = gson.fromJson(json.toString(), ExpiredJson.class);
        try {
            expiredJson.getNote().isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }


}
