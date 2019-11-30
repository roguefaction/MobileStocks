package vu.project.mobilestockstest.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
    }

    public static String generateSymbolSearchUrl(String query) {
        return "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + query + "&apikey=" + Constants.API_KEY;
    }

    public static String generateGlobalQuoteUrl(String query) {
        return "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + query + "&apikey=" + Constants.API_KEY;
    }

    public static String generateTimeSeriesDailyUrl(String query) {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + query + "&apikey=" + Constants.API_KEY;
    }

    public static String generateTimeSeriesWeeklyUrl(String query) {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + query + "&outputsize=full&apikey=" + Constants.API_KEY;
    }

    public static String generateIntraDailyUrl(String query) {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + query + "&interval=5min&apikey=" + Constants.API_KEY;
    }


}
