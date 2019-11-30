package vu.project.mobilestockstest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;

import vu.project.mobilestockstest.MainActivity;
import vu.project.mobilestockstest.R;
import vu.project.mobilestockstest.model.PortfolioItem;
import vu.project.mobilestockstest.model.PortfolioListWrapper;
import vu.project.mobilestockstest.util.Constants;
import vu.project.mobilestockstest.validator.PortfolioValidator;

public class PortfolioActivity extends AppCompatActivity {

    private PortfolioListWrapper portfolioListWrapper;

    private Button createPortfolioBtn;
    private EditText portfolioInputField;
    private Button confirmCreatePortfolioBtn;
    private Button cancelCreatePortfolioBtn;

    private TextView portfolioListErrorMessage;

    private ListView portfolioListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        createPortfolioBtn = findViewById(R.id.createPortfolioButton);
        portfolioInputField = findViewById(R.id.newPortfolioNameInput);
        confirmCreatePortfolioBtn = findViewById(R.id.finishPortfolioCreationButton);
        cancelCreatePortfolioBtn = findViewById(R.id.portfolioCancelButton);
        portfolioListView = findViewById(R.id.portfolioList);

        portfolioListErrorMessage = findViewById(R.id.portfolioListErrorMessage);
        portfolioListErrorMessage.setVisibility(View.INVISIBLE);

        if (new File(getFilesDir(), Constants.DEFAULT_PORTFOLIO_FILE_NAME).exists()) {
            portfolioListWrapper = PortfolioListWrapper.load(Constants.DEFAULT_PORTFOLIO_FILE_NAME, this);
            populateListView();
        } else portfolioListWrapper = new PortfolioListWrapper();

        portfolioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent intent = new Intent(PortfolioActivity.this, PortfolioItemActivity.class);
                intent.putExtra("portfolio", (Serializable) portfolioListView.getItemAtPosition(position));
                intent.putExtra("portfolioListWrapper", (Serializable) portfolioListWrapper);
                startActivity(intent);
            }
        });


        createPortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPortfolioBtn.setVisibility(View.INVISIBLE);
                portfolioInputField.setVisibility(View.VISIBLE);
                confirmCreatePortfolioBtn.setVisibility(View.VISIBLE);
                cancelCreatePortfolioBtn.setVisibility(View.VISIBLE);
            }
        });

        cancelCreatePortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPortfolioBtn.setVisibility(View.VISIBLE);
                portfolioInputField.setVisibility(View.INVISIBLE);
                confirmCreatePortfolioBtn.setVisibility(View.INVISIBLE);
                cancelCreatePortfolioBtn.setVisibility(View.INVISIBLE);
            }

        });

        confirmCreatePortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPortfolioCreatedState();
                PortfolioItem newPortfolio = new PortfolioItem(portfolioInputField.getText().toString());

                if (!PortfolioValidator.isPortfolioNameValid(newPortfolio.getName(), portfolioListWrapper)) {
                    printDuplicateNameError(newPortfolio.getName());

                } else {
                    portfolioListWrapper.getPortfolioItems().add(newPortfolio);
                    portfolioListWrapper.save(Constants.DEFAULT_PORTFOLIO_FILE_NAME, PortfolioActivity.this);
                    makeErrorsInvisible();
                    populateListView();
                }
            }
        });



        Button navigateBrowser = (Button) findViewById(R.id.browseButton3);
        navigateBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        Button navigateConverter = (Button) findViewById(R.id.converterButton3);
        navigateConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrencyConverterActivity();
            }
        });

    }

    public void makeErrorsInvisible(){
        portfolioListErrorMessage.setVisibility(View.INVISIBLE);
        portfolioListErrorMessage.setVisibility(View.INVISIBLE);
    }

    public void addItemAndSavePortfolio(PortfolioItem portfolio) {
        portfolioListWrapper.getPortfolioItems().add(portfolio);
        portfolioListWrapper.save(Constants.DEFAULT_PORTFOLIO_FILE_NAME, PortfolioActivity.this);
        makeErrorsInvisible();
    }

    private void setPortfolioCreatedState() {
        createPortfolioBtn.setVisibility(View.VISIBLE);
        portfolioInputField.setVisibility(View.INVISIBLE);
        confirmCreatePortfolioBtn.setVisibility(View.INVISIBLE);
        cancelCreatePortfolioBtn.setVisibility(View.INVISIBLE);
    }

    private void openMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void openCurrencyConverterActivity() {

        Intent intent = new Intent(this, CurrencyCalculatorActivity.class);
        startActivity(intent);

    }

    private void populateListView() {

        ArrayAdapter<PortfolioItem> adapter = new ArrayAdapter<>(PortfolioActivity.this, android.R.layout.simple_list_item_1, portfolioListWrapper.getPortfolioItems());
        portfolioListView.setAdapter(adapter);
        portfolioListView.setVisibility(View.VISIBLE);
    }

    private void printDuplicateNameError(String name){
        portfolioListErrorMessage.setText("Portfolio with name \"" + name + "\" already exists");
        portfolioListErrorMessage.setVisibility(View.VISIBLE);
    }

}
