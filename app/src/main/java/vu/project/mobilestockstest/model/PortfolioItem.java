package vu.project.mobilestockstest.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PortfolioItem implements Serializable {

    private String name;

    private List<SearchedStockItem> stocks;


    public PortfolioItem(String name) {
        this.name = name;
        this.stocks = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<SearchedStockItem> getStocks() {
        return stocks;
    }

    public void setStocks(List<SearchedStockItem> stocks) {
        this.stocks = stocks;
    }

    @NotNull
    @Override
    public String toString() {
        return this.name;
    }


}
