package vu.project.mobilestockstest.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Iterator;

import vu.project.mobilestockstest.MainActivity;
import vu.project.mobilestockstest.R;
import vu.project.mobilestockstest.model.PortfolioItem;
import vu.project.mobilestockstest.model.PortfolioListWrapper;
import vu.project.mobilestockstest.model.SearchedStockItem;
import vu.project.mobilestockstest.util.Constants;
import vu.project.mobilestockstest.validator.PortfolioValidator;

public class PortfolioItemActivity extends AppCompatActivity {

    private PortfolioListWrapper portfolioListWrapper;
    private PortfolioItem currentPortfolio;

    private TextView portfolioName;
    private ListView stockListView;

    private Button addStockButton;
    private Button editPortfolioButton;
    private Button deletePortfolioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_item);

        final Button navigateConverter = (Button) findViewById(R.id.converterButton4);
        final Button navigatePortfolio = (Button) findViewById(R.id.portfolioButton4);
        final Button navigateBrowser = (Button) findViewById(R.id.browseButton4);

        editPortfolioButton = findViewById(R.id.editPortfolioButton);
        deletePortfolioButton = findViewById(R.id.deletePortfolioButton);

        portfolioName = findViewById(R.id.portfolioTitle);
        stockListView = findViewById(R.id.portfolioStockList);
        addStockButton = findViewById(R.id.addStockButton);

        portfolioListWrapper = PortfolioListWrapper.load(Constants.DEFAULT_PORTFOLIO_FILE_NAME, this);
        currentPortfolio = (PortfolioItem) getIntent().getSerializableExtra("portfolio");
        portfolioName.setText(currentPortfolio.getName());

        if (!currentPortfolio.getStocks().isEmpty()) {
            populateListView();
        }

        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PortfolioItemActivity.this, StockActivity.class);
                intent.putExtra("stock", (Serializable) stockListView.getItemAtPosition(position));
                startActivity(intent);

            }
        });

        stockListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final SearchedStockItem selectedItem = (SearchedStockItem) stockListView.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(PortfolioItemActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to remove stock \"" + selectedItem.getSymbol() + "\"?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for (PortfolioItem item : portfolioListWrapper.getPortfolioItems()) {
                                    if (item.getName().equals(currentPortfolio.getName())) {

                                        Iterator<SearchedStockItem> iterator = item.getStocks().iterator();
                                        while (iterator.hasNext()) {
                                            if (iterator.next().getSymbol().equals(selectedItem.getSymbol()))
                                                iterator.remove();
                                        }
                                        currentPortfolio = item;
                                    }

                                }
                                portfolioListWrapper.save(Constants.DEFAULT_PORTFOLIO_FILE_NAME, PortfolioItemActivity.this);
                                populateListView();

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        deletePortfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PortfolioItemActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to delete \"" + currentPortfolio.getName() + "\"?");
                builder.setMessage("This action cannot be reverted");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Iterator<PortfolioItem> iter = portfolioListWrapper.getPortfolioItems().iterator();

                                while (iter.hasNext()) {
                                    if (iter.next().getName().equals(currentPortfolio.getName())) {
                                        iter.remove();
                                    }
                                }

                                portfolioListWrapper.save(Constants.DEFAULT_PORTFOLIO_FILE_NAME, PortfolioItemActivity.this);
                                openPortfolioActivity();

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        editPortfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PortfolioItemActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Specify new portfolio name");
                builder.setMessage("");
                final EditText input = new EditText(PortfolioItemActivity.this);
                builder.setView(input);
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!PortfolioValidator.isPortfolioNameValid(input.getText().toString(), portfolioListWrapper)) {
                            TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
                            dialogMessage.setText("Name : \"" + input.getText().toString() + "\" is already taken");
                        } else {
                            Iterator<PortfolioItem> iter = portfolioListWrapper.getPortfolioItems().iterator();
                            while (iter.hasNext()) {
                                PortfolioItem item = iter.next();
                                if (item.getName().equals(currentPortfolio.getName())) {
                                    item.setName(input.getText().toString());
                                }
                            }

                            portfolioListWrapper.save(Constants.DEFAULT_PORTFOLIO_FILE_NAME, PortfolioItemActivity.this);
                            openPortfolioActivity();

                        }

                    }
                });
            }
        });


        addStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity();
            }
        });

        navigateBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
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

    private void populateListView() {

        ArrayAdapter<SearchedStockItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentPortfolio.getStocks());
        stockListView.setAdapter(adapter);
        stockListView.setVisibility(View.VISIBLE);
    }

    private void openSearchActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("portfolio", currentPortfolio);
        startActivity(intent);

    }

    private void openCurrencyConverterActivity() {

        Intent intent = new Intent(this, CurrencyCalculatorActivity.class);
        startActivity(intent);

    }

    private void openPortfolioActivity() {

        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);

    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
