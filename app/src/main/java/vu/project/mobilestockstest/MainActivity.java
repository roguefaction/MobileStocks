package vu.project.mobilestockstest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import vu.project.mobilestockstest.activity.CurrencyCalculatorActivity;
import vu.project.mobilestockstest.activity.PortfolioActivity;
import vu.project.mobilestockstest.activity.PortfolioItemActivity;
import vu.project.mobilestockstest.activity.StockActivity;
import vu.project.mobilestockstest.model.PortfolioItem;
import vu.project.mobilestockstest.model.PortfolioListWrapper;
import vu.project.mobilestockstest.model.SearchedStockItem;
import vu.project.mobilestockstest.model.SearchedStockItemWrapper;
import vu.project.mobilestockstest.util.Constants;
import vu.project.mobilestockstest.util.HttpUtils;
import vu.project.mobilestockstest.validator.JsonValidator;

public class MainActivity extends AppCompatActivity {

    private PortfolioListWrapper portfolioListWrapper;

    private TextView searchPromptTextView;

    private MaterialSearchView searchView;
    private ListView stockListView;
    private TextView loadingText;

    private PortfolioItem currentPortfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchPromptTextView = findViewById(R.id.searchPromptTextView);

        setContentView(R.layout.activity_main);

        // navigation buttons and listeners

        final Button navigateConverter = (Button) findViewById(R.id.converterButton);
        final Button navigatePortfolio = (Button) findViewById(R.id.portfolioButton);

        loadingText = findViewById(R.id.loadingText3);
        loadingText.setVisibility(View.INVISIBLE);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Constants.SEARCH_BAR_TITLE);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        searchView = (MaterialSearchView) findViewById(R.id.search_view);


        stockListView = (ListView) findViewById(R.id.stockListView);
        stockListView.setVisibility(View.INVISIBLE);

        currentPortfolio = (PortfolioItem) getIntent().getSerializableExtra("portfolio");

        if (currentPortfolio == null) {
            stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    Intent intent = new Intent(MainActivity.this, StockActivity.class);
                    intent.putExtra("stock", (Serializable) stockListView.getItemAtPosition(position));
                    startActivity(intent);
                }
            });

        } else {

            stockListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                            portfolioListWrapper = PortfolioListWrapper.load(Constants.DEFAULT_PORTFOLIO_FILE_NAME, MainActivity.this);

                            for (PortfolioItem portfolio : portfolioListWrapper.getPortfolioItems()) {
                                if (portfolio.getName().equals(currentPortfolio.getName())) {
                                    portfolio.getStocks().add((SearchedStockItem) stockListView.getItemAtPosition(position));
                                    currentPortfolio = portfolio;
                                }
                            }
                    portfolioListWrapper.save(Constants.DEFAULT_PORTFOLIO_FILE_NAME, MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, PortfolioItemActivity.class);
                    intent.putExtra("portfolio", (Serializable) currentPortfolio);
                    startActivity(intent);
                }
            });

        }

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                setListAsLoading();

                HttpUtils.get(HttpUtils.generateSymbolSearchUrl(query), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        if (JsonValidator.wasJsonCallLimitReached(response)) {
                            setLimitReachedState();
                            return;

                        }
                        setQueryReceivedState();
                        Gson gson = new Gson();

                        SearchedStockItemWrapper searchedStocks = gson.fromJson(response.toString(), SearchedStockItemWrapper.class);
                        populateSearchList(searchedStocks.getBestMatches());

                    }

                    @Override
                    public void onFailure(int statusCoIf, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        loadingText.setText(Constants.STOCKS_FAILED_TO_LOAD_MESSAGE);
                        loadingText.setVisibility(View.VISIBLE);
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        navigateConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrencyConverterActivity();
            }
        });

        navigatePortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPortfolioActivity();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    private void setQueryReceivedState() {
        loadingText.setVisibility(View.INVISIBLE);
        searchPromptTextView.setVisibility(View.INVISIBLE);
        stockListView.setVisibility(View.VISIBLE);
    }

    private void populateSearchList(List<SearchedStockItem> list) {
        ArrayAdapter<SearchedStockItem> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        stockListView.setAdapter(adapter);
    }


    private void openCurrencyConverterActivity() {

        Intent intent = new Intent(this, CurrencyCalculatorActivity.class);
        startActivity(intent);

    }

    private void openPortfolioActivity() {

        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);

    }

    private void setListAsLoading() {
        loadingText.setVisibility(View.VISIBLE);
        loadingText.setText(Constants.STOCKS_LOADING_MESSAGE);
        searchPromptTextView.setVisibility(View.INVISIBLE);
        ArrayAdapter<SearchedStockItem> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, new ArrayList<SearchedStockItem>());
        stockListView.setAdapter(adapter);
    }

    private void setLimitReachedState() {
        loadingText.setText(Constants.API_CALL_LIMIT_REACHED_MESSAGE);
        loadingText.setVisibility(View.VISIBLE);
        searchPromptTextView.setVisibility(View.INVISIBLE);
    }


}
