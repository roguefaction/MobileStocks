package vu.project.mobilestockstest.recurrenceplot;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

// corresponds to big D vector
public class Vector {

    @Getter
    private Double[] values;

    public Vector(Double... args) {
        if (args.length == 0)
            throw new IllegalArgumentException("Cannot construct a vector with no values");
        values = args;
    }

    public Vector(List<Double> args) {
        values = new Double[args.size()];
        args.toArray(values);
    }

    public double get(int index) {
        return values[index];
    }

    public int getDimensions() {
        return values.length;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
