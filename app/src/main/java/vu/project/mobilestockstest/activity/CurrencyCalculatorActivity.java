package vu.project.mobilestockstest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import vu.project.mobilestockstest.MainActivity;
import vu.project.mobilestockstest.R;
import vu.project.mobilestockstest.util.CurrencyConverter;
import vu.project.mobilestockstest.util.HttpUtils;

public class CurrencyCalculatorActivity extends AppCompatActivity {

    private TextView currencyErrorMessage;
    ArrayAdapter<CharSequence> currency_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_calculator);

        final Spinner inputCurrency = populateSpinner((Spinner) findViewById(R.id.inputCurrency));
        final Spinner outputCurrency = populateSpinner((Spinner) findViewById(R.id.outputCurrency));
        final EditText inputAmount = (EditText) findViewById(R.id.inputAmount);
        final EditText outputAmount = (EditText) findViewById(R.id.outputAmount);

        currencyErrorMessage = findViewById(R.id.currencyErrorMessage);

        String currency = (String) getIntent().getSerializableExtra("currency");
        String amount = (String) getIntent().getSerializableExtra("amount");

        if(isPricePresent(amount)){
            inputAmount.setText(amount);
            inputCurrency.setSelection(currency_adapter.getPosition(currency));
        }

        outputAmount.setKeyListener(null);

        Button convertButton = (Button) findViewById(R.id.convertButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNullOrWhitespace(inputAmount.getText().toString())) {
                    currencyErrorMessage.setText("Please enter a valid input amount");
                    currencyErrorMessage.setVisibility(View.VISIBLE);
                    return;
                }

                HttpUtils.get("https://api.exchangeratesapi.io/latest?base=" + inputCurrency.getSelectedItem().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        currencyErrorMessage.setVisibility(View.INVISIBLE);
                        // If the response is JSONObject instead of expected JSONArray
                        JsonObject currencyJson = new JsonParser().parse(response.toString()).getAsJsonObject();

                        CurrencyConverter currencyConverter = new CurrencyConverter(currencyJson);

                        String outputCurrencyString = outputCurrency.getSelectedItem().toString();
                        double convertedAmount = currencyConverter.convertAmountToCurrency(Double.parseDouble(inputAmount.getText().toString()), outputCurrencyString);
                        outputAmount.setText(Double.toString(convertedAmount), TextView.BufferType.EDITABLE);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        currencyErrorMessage.setText("Failed to get currency data");
                        currencyErrorMessage.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        Button navigateBrowser = (Button) findViewById(R.id.browseButton2);
        navigateBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        Button navigatePortfolio = (Button) findViewById(R.id.portfolioButton2);
        navigatePortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPortfolioActivity();
            }
        });

    }

    public boolean isPricePresent(String price) {

        try {
            price.equals(0);
        } catch (NullPointerException e) {
            return false;
        }
        return true;

    }

    private Spinner populateSpinner(Spinner spinnerToPopulate) {

        currency_adapter = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        currency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToPopulate.setAdapter(currency_adapter);

        return spinnerToPopulate;
    }

    private void openMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public boolean isNullOrWhitespace(String string) {
        return string.isEmpty() || string.trim().length() == 0;
    }

    private void openPortfolioActivity() {

        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);

    }

}
