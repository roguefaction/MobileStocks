package vu.project.mobilestockstest.util;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    private DataUtils() {
    }

    public static List<Double> downSampleData(List<Double> data, int desiredAmount) {
        List<Double> result = new ArrayList<>();
        int sampleSize = (int) data.size() / desiredAmount;

        for (int i = 0; i < data.size(); i += sampleSize)
            result.add(data.get(i));

        return result;
    }
}
