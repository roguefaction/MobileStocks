package vu.project.mobilestockstest.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import vu.project.mobilestockstest.R;
import vu.project.mobilestockstest.model.graph.XAxisValueFormatter;
import vu.project.mobilestockstest.model.GlobalQuoteWrapper;
import vu.project.mobilestockstest.model.HistoricalStockData;
import vu.project.mobilestockstest.model.SearchedStockItem;
import vu.project.mobilestockstest.model.StockData;
import vu.project.mobilestockstest.recurrenceplot.RecurrencePlot;
import vu.project.mobilestockstest.util.Constants;
import vu.project.mobilestockstest.util.DataUtils;
import vu.project.mobilestockstest.util.HttpUtils;
import vu.project.mobilestockstest.validator.JsonValidator;

public class StockActivity extends AppCompatActivity {


    private List<Double> rpSignal = new ArrayList<>();
    private RecurrencePlot plot;

    // components declaration

    private TextView title;
    private LineChart chart;
    private TextView subRowText;
    private TextView cornerText;
    private TextView updatedText;

    // buttons declaration;

    private Button graphWeekBtn;
    private Button graphMonthBtn;
    private Button graph3MonthBtn;

    private Button refreshButton;
    private Button jumpConverterButton;
    private Button rpButton;

    // data objects declaration

    private GlobalQuoteWrapper stockQuotePackage;
    private List<HistoricalStockData> historicalDataListDay;
    private List<HistoricalStockData> historicalDataListWeek;
    private List<HistoricalStockData> historicalDataListIntraDay;
    private SearchedStockItem searchedStockItem;

    // quote values
    private ImageView rpView;

    private TextView symbolValue;
    private TextView openValue;
    private TextView highValue;
    private TextView lowValue;
    private TextView volumeValue;
    private TextView changeValue;

    // quote names

    private TextView openName;
    private TextView highName;
    private TextView lowName;
    private TextView volumeName;


    private TextView progressTextQuote;
    private TextView progressTextGraph;
    private boolean recurrencePlotVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        chart = (LineChart) findViewById(R.id.chart);
        searchedStockItem = (SearchedStockItem) getIntent().getSerializableExtra("stock");

        subRowText = findViewById(R.id.subRowText);
        subRowText.setText(searchedStockItem.getCurrency());
        subRowText.setVisibility(View.VISIBLE);

        cornerText = findViewById(R.id.stockCornerText);
        updatedText = findViewById(R.id.updatedTimerText);


        graphWeekBtn = findViewById(R.id.hist1button);
        graphMonthBtn = findViewById(R.id.hist2button);
        graph3MonthBtn = findViewById(R.id.hist3button);
        graph3MonthBtn = findViewById(R.id.hist3button);

        rpView = findViewById(R.id.imageView1);

        rpButton = findViewById(R.id.rpButton);
        refreshButton = findViewById(R.id.refreshButton);
        jumpConverterButton = findViewById(R.id.jumpConverterButton);

        // initialize data arrays

        historicalDataListDay = new ArrayList<>();
        historicalDataListWeek = new ArrayList<>();
        historicalDataListIntraDay = new ArrayList<>();

        symbolValue = findViewById(R.id.symbolValue);
        openValue = findViewById(R.id.openValue);
        highValue = findViewById(R.id.highValue);
        lowValue = findViewById(R.id.lowValue);
        volumeValue = findViewById(R.id.volumeValue);
        changeValue = findViewById(R.id.changeValue);

        openName = findViewById(R.id.openName);
        highName = findViewById(R.id.highName);
        lowName = findViewById(R.id.lowName);
        volumeName = findViewById(R.id.volumeName);


        progressTextQuote = findViewById(R.id.loadingText);
        progressTextGraph = findViewById(R.id.loadingText2);

        progressTextQuote.setVisibility(View.INVISIBLE);
        progressTextGraph.setVisibility(View.INVISIBLE);

        // set title

        title = findViewById(R.id.stockName);
        title.setText(searchedStockItem.getName());

        // fetch required data

        setChartInvisible();
        setButtonsInvisible();
        setQuoteDataInvisible();

        progressTextQuote.setText(Constants.QUOTE_DATA_LOADING_MESSAGE);
        progressTextQuote.setVisibility(View.VISIBLE);

        progressTextGraph.setText(Constants.HISTORICAL_DATA_LOADING_MESSAGE);
        progressTextGraph.setVisibility(View.VISIBLE);


        drawWeekGraph();
        fetchAndPopulateQuoteData();

        graphWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawWeekGraph();
            }
        });

        graphMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawMonthGraph();
            }
        });

        graph3MonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3MonthGraph();
            }
        });

        rpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recurrencePlotVisible)
                    drawRecurrencePlot();
                else hideRecurrencePlot();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshQuoteWithLatestValues();
            }
        });

        jumpConverterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrencyConverterActivity();
            }
        });

    }

    private void drawRecurrencePlot() {
//        this.rpSignal = DataUtils.downSampleData(this.rpSignal, 1000);
        plot = new RecurrencePlot(this.rpSignal);
//        plot.setShouldThresholdCalibrate(true);
        plot.setColoredPlot(true);
        Bitmap image = plot.generatePlot();
        rpView.setImageBitmap(image);
        rpView.setVisibility(View.VISIBLE);
        recurrencePlotVisible = true;
    }

    private void hideRecurrencePlot() {
        rpView.setVisibility(View.INVISIBLE);
        recurrencePlotVisible = false;
    }

    private void populateHistoricalDataListWithJson(JsonObject data, List<HistoricalStockData> dataList) throws NullPointerException {
        rpSignal = new ArrayList<>();
        Gson gson = new Gson();
        Set<Map.Entry<String, JsonElement>> entrySet = data.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            StockData dataEntry = gson.fromJson(entry.getValue().toString(), StockData.class);
            HistoricalStockData hsd = new HistoricalStockData(entry.getKey(), dataEntry);
            appendStockMetric(hsd);
            dataList.add(hsd);
        }

    }

    private void appendStockMetric(HistoricalStockData hsd) {
        rpSignal.add(hsd.getData().getHigh());
    }


    private void fetchAndPopulateQuoteData() {
        HttpUtils.get(HttpUtils.generateGlobalQuoteUrl(searchedStockItem.getSymbol()), new JsonHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (JsonValidator.wasJsonCallLimitReached(response)) {
                    setCallLimitReachedStateQuote();
                    return;
                }

                setQuoteDataVisible();
                progressTextQuote.setVisibility(View.INVISIBLE);
                Gson gson = new Gson();
                stockQuotePackage = gson.fromJson(response.toString(), GlobalQuoteWrapper.class);
                populateQuoteData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                setQuoteDataInvisible();
                showQuoteDataErrorMessage();
            }
        });
    }

    private void refreshQuoteWithLatestValues() {
        HttpUtils.get(HttpUtils.generateIntraDailyUrl(searchedStockItem.getSymbol()), new JsonHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                removeCallLimitReachedStateQuote();

                if (JsonValidator.wasJsonCallLimitReached(response)) {
                    setCallLimitReachedStateQuote();
                    return;
                }

                setQuoteDataVisible();
                JsonObject historicalDataJson = new JsonParser().parse(response.toString()).getAsJsonObject();
                JsonObject dataArrayJson = historicalDataJson.getAsJsonObject("Time Series (5min)");

                populateHistoricalDataListWithJson(dataArrayJson, historicalDataListIntraDay);
                updateQuoteWithIntraDayData();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                setQuoteDataInvisible();
                showQuoteDataErrorMessage();
            }
        });
    }

    private void updateQuoteWithIntraDayData() {
        updatedText.setVisibility(View.VISIBLE);
        updatedText.setText(historicalDataListIntraDay.get(0).getDate());
        openValue.setText(Double.toString(historicalDataListIntraDay.get(0).getData().getOpen()));
        highValue.setText(Double.toString(historicalDataListIntraDay.get(0).getData().getHigh()));
        lowValue.setText(Double.toString(historicalDataListIntraDay.get(0).getData().getLow()));
        volumeValue.setText(Double.toString(historicalDataListIntraDay.get(0).getData().getVolume()));
    }


    private void drawWeekGraph() {

        if (!historicalDataListDay.isEmpty()) {
            drawGraph(Constants.WEEK_GRAPH_RANGE, historicalDataListDay);
        } else {
            setChartInvisible();
            progressTextGraph.setVisibility(View.VISIBLE);
            HttpUtils.get(HttpUtils.generateTimeSeriesDailyUrl(searchedStockItem.getSymbol()), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    if (JsonValidator.wasJsonCallLimitReached(response)) {
                        setCallLimitReachedStateGraph();
                        return;
                    }

                    progressTextGraph.setVisibility(View.INVISIBLE);
                    setChartVisible();
                    setButtonsVisible();
                    JsonObject historicalDataJson = new JsonParser().parse(response.toString()).getAsJsonObject();
                    JsonObject dataArrayJson = historicalDataJson.getAsJsonObject("Time Series (Daily)");

                    populateHistoricalDataListWithJson(dataArrayJson, historicalDataListDay);

                    drawGraph(Constants.WEEK_GRAPH_RANGE, historicalDataListDay);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    setChartInvisible();
                    showGraphDataErrorMessage();
                }
            });
        }

    }


    private void setCallLimitReachedStateGraph() {
        setChartInvisible();
        setButtonsInvisible();

        progressTextGraph.setText(Constants.API_CALL_LIMIT_REACHED_MESSAGE);
        progressTextGraph.setVisibility(View.VISIBLE);
    }

    private void setCallLimitReachedStateQuote() {
        setQuoteDataInvisible();
        progressTextQuote.setText(Constants.API_CALL_LIMIT_REACHED_MESSAGE);
        progressTextQuote.setVisibility(View.VISIBLE);
    }

    private void removeCallLimitReachedStateQuote() {
        setQuoteDataVisible();
        progressTextQuote.setVisibility(View.INVISIBLE);
    }

    private void drawMonthGraph() {

        if (!historicalDataListWeek.isEmpty()) {
            drawGraph(Constants.MONTH_GRAPH_RANGE, historicalDataListWeek);
        } else {
            setChartInvisible();
            progressTextGraph.setVisibility(View.VISIBLE);
            HttpUtils.get(HttpUtils.generateTimeSeriesWeeklyUrl(searchedStockItem.getSymbol()), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    if (JsonValidator.wasJsonCallLimitReached(response)) {
                        setCallLimitReachedStateGraph();
                        return;
                    }

                    progressTextGraph.setVisibility(View.INVISIBLE);
                    setChartVisible();
                    setButtonsVisible();
                    JsonObject historicalData = new JsonParser().parse(response.toString()).getAsJsonObject();
                    JsonObject dataArrayJson = historicalData.getAsJsonObject("Weekly Time Series");
                    populateHistoricalDataListWithJson(dataArrayJson, historicalDataListWeek);

                    drawGraph(Constants.MONTH_GRAPH_RANGE, historicalDataListWeek);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    setChartInvisible();
                    showGraphDataErrorMessage();
                }
            });
        }

    }

    private void draw3MonthGraph() {

        if (!historicalDataListWeek.isEmpty()) {
            drawGraph(Constants.MONTH_3_GRAPH_RANGE, historicalDataListWeek);
        } else {
            setChartInvisible();
            progressTextGraph.setVisibility(View.VISIBLE);
            HttpUtils.get(HttpUtils.generateTimeSeriesWeeklyUrl(searchedStockItem.getSymbol()), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    if (JsonValidator.wasJsonCallLimitReached(response)) {
                        setCallLimitReachedStateGraph();
                        return;
                    }

                    progressTextGraph.setVisibility(View.INVISIBLE);
                    setChartVisible();
                    setButtonsVisible();
                    JsonObject historicalData = new JsonParser().parse(response.toString()).getAsJsonObject();
                    JsonObject dataArrayJson = historicalData.getAsJsonObject("Weekly Time Series");
                    populateHistoricalDataListWithJson(dataArrayJson, historicalDataListWeek);

                    drawGraph(Constants.MONTH_3_GRAPH_RANGE, historicalDataListWeek);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    setChartInvisible();
                    showGraphDataErrorMessage();

                }
            });
        }

    }

    private void populateQuoteData() {
        cornerText.setText(stockQuotePackage.getGlobalQuote().getPrice().substring(0, stockQuotePackage.getGlobalQuote().getPrice().length() - 2));
        symbolValue.setText(stockQuotePackage.getGlobalQuote().getSymbol());
        openValue.setText(stockQuotePackage.getGlobalQuote().getOpen());
        highValue.setText(stockQuotePackage.getGlobalQuote().getHigh());
        lowValue.setText(stockQuotePackage.getGlobalQuote().getLow());
        volumeValue.setText(stockQuotePackage.getGlobalQuote().getVolume());
        changeValue.setText(stockQuotePackage.getGlobalQuote().getChange());
        setCorrectColor(stockQuotePackage.getGlobalQuote().getChange());

    }

    private void setCorrectColor(String changeAsString) {
        if (Double.parseDouble(changeAsString) < 0) {
            changeValue.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        changeValue.setTextColor(getResources().getColor(R.color.colorAccent2));
    }


    private void drawGraph(int range, List<HistoricalStockData> dataList) {
        chart.setVisibility(View.VISIBLE);
        List<HistoricalStockData> subList = dataList.subList(0, range);
        subList = reverseList(subList);

        List<Entry> closeEntries = new ArrayList<>();
        List<Entry> highEntries = new ArrayList<>();
        List<Entry> openEntries = new ArrayList<>();
        List<Entry> lowEntries = new ArrayList<>();

        int index = 0;
        final List<String> labels = new ArrayList<>();

        for (HistoricalStockData entry : subList) {
            closeEntries.add(new Entry(index, (float) entry.getData().getClose()));
            highEntries.add(new Entry(index, (float) entry.getData().getHigh()));
            openEntries.add(new Entry(index, (float) entry.getData().getOpen()));
            lowEntries.add(new Entry(index, (float) entry.getData().getLow()));
            labels.add(entry.getDate());
            index++;
        }

        LineDataSet closeDataSet = formatLineDataSet(closeEntries, "Close", ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        LineDataSet openDataSet = formatLineDataSet(openEntries, "Open", ResourcesCompat.getColor(getResources(), R.color.colorAccent2, null));
        LineDataSet lowDataSet = formatLineDataSet(lowEntries, "Low", ResourcesCompat.getColor(getResources(), R.color.colorAccent3, null));
        LineDataSet highDataSet = formatLineDataSet(highEntries, "High", ResourcesCompat.getColor(getResources(), R.color.colorAccent4, null));

        addLabelsToGraph(labels.toArray(new String[0]));
        formatGraph();

        LineData lineData = new LineData();
        lineData.addDataSet(lowDataSet);
        lineData.addDataSet(highDataSet);
        lineData.addDataSet(openDataSet);
        lineData.addDataSet(closeDataSet);

        chart.setData(lineData);
        chart.invalidate();
    }


    private void addLabelsToGraph(String[] labelsArray) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new XAxisValueFormatter(labelsArray));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void formatGraph() {
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
    }

    private LineDataSet formatLineDataSet(List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setDrawValues(false);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        return dataSet;
    }

    private static <T> List<T> reverseList(List<T> list) {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }

    private void setQuoteDataInvisible() {

        updatedText.setVisibility(View.INVISIBLE);
        symbolValue.setVisibility(View.INVISIBLE);
        openValue.setVisibility(View.INVISIBLE);
        highValue.setVisibility(View.INVISIBLE);
        lowValue.setVisibility(View.INVISIBLE);
        volumeValue.setVisibility(View.INVISIBLE);
        changeValue.setVisibility(View.INVISIBLE);
        subRowText.setVisibility(View.INVISIBLE);
        // quote names

//        symbolName.setVisibility(View.INVISIBLE);
        openName.setVisibility(View.INVISIBLE);
        highName.setVisibility(View.INVISIBLE);
        lowName.setVisibility(View.INVISIBLE);
        volumeName.setVisibility(View.INVISIBLE);
//        changeName.setVisibility(View.INVISIBLE);
    }

    private void setQuoteDataVisible() {
        symbolValue.setVisibility(View.VISIBLE);
        openValue.setVisibility(View.VISIBLE);
        highValue.setVisibility(View.VISIBLE);
        lowValue.setVisibility(View.VISIBLE);
        volumeValue.setVisibility(View.VISIBLE);
        changeValue.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        cornerText.setVisibility(View.VISIBLE);
        subRowText.setVisibility(View.VISIBLE);

        // quote names

//        symbolName.setVisibility(View.VISIBLE);
        openName.setVisibility(View.VISIBLE);
        highName.setVisibility(View.VISIBLE);
        lowName.setVisibility(View.VISIBLE);
        volumeName.setVisibility(View.VISIBLE);
//        changeName.setVisibility(View.VISIBLE);
    }

    private void showQuoteDataErrorMessage() {
        progressTextQuote.setText(Constants.QUOTE_DATA_FAILED_TO_LOAD_MESSAGE);
        progressTextQuote.setVisibility(View.VISIBLE);
    }

    private void setChartInvisible() {
        chart.setVisibility(View.INVISIBLE);
    }

    private void setButtonsInvisible() {
        graphWeekBtn.setVisibility(View.INVISIBLE);
        graphMonthBtn.setVisibility(View.INVISIBLE);
        graph3MonthBtn.setVisibility(View.INVISIBLE);
        jumpConverterButton.setVisibility(View.INVISIBLE);
    }

    private void setChartVisible() {
        chart.setVisibility(View.VISIBLE);
    }

    private void setButtonsVisible() {
        graphWeekBtn.setVisibility(View.VISIBLE);
        graphMonthBtn.setVisibility(View.VISIBLE);
        graph3MonthBtn.setVisibility(View.VISIBLE);
        jumpConverterButton.setVisibility(View.VISIBLE);
    }

    private void showGraphDataErrorMessage() {
        progressTextGraph.setText(Constants.HISTORICAL_DATA_FAILED_TO_LOAD_MESSAGE);
        progressTextGraph.setVisibility(View.VISIBLE);
    }

    private void openCurrencyConverterActivity() {

        Intent intent = new Intent(this, CurrencyCalculatorActivity.class);
        intent.putExtra("amount", (stockQuotePackage.getGlobalQuote().getPrice()));
        intent.putExtra("currency", (searchedStockItem.getCurrency()));
        startActivity(intent);

    }


}
