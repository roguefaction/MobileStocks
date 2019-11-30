package vu.project.mobilestockstest.model.graph;

import com.github.mikephil.charting.formatter.ValueFormatter;


public class XAxisValueFormatter extends ValueFormatter {

    private String[] mValues;

    public XAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value) {
        return mValues[(int) value];
    }
}