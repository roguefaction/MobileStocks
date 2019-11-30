package vu.project.mobilestockstest.recurrenceplot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistanceUtil {

    private DistanceUtil() {
    }

    public static double euclideanDistance(Vector v1, Vector v2) {
        verifyDimensions(v1, v2);

        int dimensions = v1.getDimensions();
        double sumOfSquares = 0;

        for (int i = 0; i < dimensions; i++) {
            sumOfSquares += Math.pow((v1.get(i) - v2.get(i)), 2);
        }

        return Math.sqrt(sumOfSquares);
    }

    public static double manhattanDistance(Vector v1, Vector v2) {
        verifyDimensions(v1, v2);

        int dimensions = v1.getDimensions();
        double result = 0;

        for (int i = 0; i < dimensions; i++) {
            result += Math.abs((v1.get(i) - v2.get(i)));
        }

        return result;
    }

    public static double maximumDistance(Vector v1, Vector v2) {
        verifyDimensions(v1, v2);

        int dimensions = v1.getDimensions();
        List<Double> listOfAbsolutes = new ArrayList<>();

        for (int i = 0; i < dimensions; i++) {
            listOfAbsolutes.add(Math.abs((v1.get(i) - v2.get(i))));
        }

        return Collections.max(listOfAbsolutes);
    }

    private static void verifyDimensions(Vector v1, Vector v2) {
        if (v1.getDimensions() != v2.getDimensions())
            throw new IllegalArgumentException("Dimensions of comparable vectors must match");
    }

    public static DistanceType parseDistanceType(String s) {
        switch (s) {
            case "manhattan":
                return DistanceType.MANHATTAN;
            case "euclidean":
                return DistanceType.EUCLIDEAN;
            case "maximum":
                return DistanceType.MAXIMUM;
            default:
                throw new IllegalArgumentException("Unknown distance type");
        }
    }

}
