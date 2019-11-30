package vu.project.mobilestockstest.recurrenceplot;

import lombok.Setter;

import android.graphics.Bitmap;
import android.graphics.Color;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurrencePlot {

    private static final int DEFAULT_DIMENSION = 2;
    private static final int DEFAULT_TIME_DELAY = 1;
    private static final int RP_POINT_DIMENSION = 1;

    private List<Double> signal;
    private List<SignalState> signalStates;
    private int imageSize;
    private int blackDotCount;

    @Setter
    private boolean coloredPlot = false;

    @Setter
    private double threshold = 1;

    @Setter
    private boolean shouldThresholdCalibrate = false;

    @Setter
    private DistanceType distanceType = DistanceType.EUCLIDEAN;

    @Setter
    private double desiredCoverage = 0.1;

    @Setter
    private double desiredCoverageErrorMargin = 0.01;

    public RecurrencePlot(List<Double> signal) {
        this.signal = signal;
        this.signalStates = constructSignalStates(signal, DEFAULT_DIMENSION, DEFAULT_TIME_DELAY);
        System.out.println("Signal states constructed with dimension: " + DEFAULT_DIMENSION + " and timeDelay: " + DEFAULT_TIME_DELAY);
    }

    public void reconstructSignalStates(int dimension, int timeDelay) {
        this.signalStates = constructSignalStates(this.signal, dimension, timeDelay);
        System.out.println("Signal states reconstructed with dimension: " + dimension + " and timeDelay: " + timeDelay);
    }

    public Bitmap generatePlot() {
        if(coloredPlot){
            System.out.println("Generating colored plot");
            return generateColoredRecurrencePlot();
        }

        if (shouldThresholdCalibrate) {
            System.out.println("Calibrating threshold");
            calibrateThreshold();
        }

        System.out.println("Generating plot");
        return generateRecurrencePlot();
    }

    private List<SignalState> constructSignalStates(List<Double> signal, int dimension, int timeDelay) {
        List<SignalState> result = new ArrayList<>();
        int adjustedSignalLength = signal.size() - ((dimension - 1) * timeDelay);
        this.imageSize = adjustedSignalLength;

        for (int j = 0; j < adjustedSignalLength; j++) {
            List<Double> signalStateValues = new ArrayList<>();
            for (int i = 0; i < dimension; i++) {
                signalStateValues.add(signal.get(j + (timeDelay * i)));
            }
            result.add(new SignalState(signalStateValues));
        }
        return result;
    }

    private void calibrateThreshold() {
        System.out.println("---------------------------------------");
        while (true) {
            Bitmap image = generateRecurrencePlot();
            double currentThreshold = calculateBlackThreshold();

            System.out.println("Threshold parameter: " + this.threshold);
            System.out.println("Current percentage: " + currentThreshold);
            System.out.println("---------------------------------------");
            if (currentThreshold > desiredCoverage + desiredCoverageErrorMargin) {
                this.threshold /= 2;
            } else if (currentThreshold < desiredCoverage - desiredCoverageErrorMargin) {
                this.threshold *= 1.5;
            } else break;

        }
    }

    private Bitmap generateColoredRecurrencePlot() {
        Bitmap image = Bitmap.createBitmap(this.imageSize, this.imageSize, Bitmap.Config.ARGB_8888);
        Map<String, Double> calc = calculateAmplitude(this.signal);

        String[] colors = new String[]{"#ffffff", "#ffffcf", "#ffff9e", "#ffff67", "#ffff00",
                "#fff500", "#ffec00", "#ffe200", "#ffd800", "#ffce00", "#ffc400", "#ffb900", "#ffaf00", "#ffa400", "#ff9900", "#ff8e00", "#ff8300", "#ff7700", "#ff6b00", "#ff5d00", "#ff4f00", "#ff3f00", "#ff2a00", "#ff0000",
                "#ff001b", "#ff002d", "#ff003d", "#ff004d", "#ff005d", "#ff006e", "#fa0080", "#f00093", "#e200a6", "#d000b9", "#b900cc", "#9b00de", "#7000ef", "#0000ff",
                "#0000bf", "#000080", "#000040", "#000000"
        };

        double distance = calc.get("max") - calc.get("min");
        double scale = distance / colors.length;

        for (int v = 0; v < colors.length; v++) {
            if (v == 0)
                this.threshold = distance * 5;
            else
                this.threshold = distance - (scale * v);

            int color = Color.parseColor(colors[v]);

            for (int i = 0; i < signalStates.size(); i++) {

                for (int j = 0; j < signalStates.size(); j++) {
                    if (isSignalRecurrent(signalStates.get(i), signalStates.get(j), this.distanceType)) {
                        drawPoint(image, i, j, color);
                    }
                }
            }
        }

        return image;
    }

    private Map<String, Double> calculateAmplitude(List<Double> data) {
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("min", Collections.min(data));
        result.put("max", Collections.max(data));
        return result;
    }

    private Bitmap generateRecurrencePlot() {
        Bitmap image = Bitmap.createBitmap(this.imageSize, this.imageSize, Bitmap.Config.ARGB_8888);
        this.blackDotCount = 0;

        for (int i = 0; i < signalStates.size(); i++) {

            for (int j = 0; j < signalStates.size(); j++) {

                if (isSignalRecurrent(signalStates.get(i), signalStates.get(j), this.distanceType)) {
                    drawPoint(image, i, j, Color.BLACK);
                    blackDotCount++;
                } else {
                    drawPoint(image, i, j, Color.WHITE);
                }
            }
        }
        return image;
    }

    private void drawPoint(Bitmap bitmap, int i, int j, int color) {
        bitmap.setPixel(i, j, color);
    }

    private double calculateBlackThreshold() {
        int totalPixels = imageSize * imageSize;
        double blackPixelCount = (double) this.blackDotCount;
        return blackPixelCount / totalPixels;
    }

    private boolean isSignalRecurrent(SignalState p1, SignalState p2, DistanceType type) {
        switch (type) {
            case EUCLIDEAN:
                return DistanceUtil.euclideanDistance(p1, p2) <= this.threshold;

            case MAXIMUM:
                return DistanceUtil.maximumDistance(p1, p2) <= this.threshold;

            case MANHATTAN:
                return DistanceUtil.manhattanDistance(p1, p2) <= this.threshold;

            default:
                throw new IllegalArgumentException("Distance specifier unknown");
        }
    }



}



