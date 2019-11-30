package vu.project.mobilestockstest.validator;

import vu.project.mobilestockstest.model.PortfolioItem;
import vu.project.mobilestockstest.model.PortfolioListWrapper;

public class PortfolioValidator {

    public static boolean isPortfolioNameValid(String name, PortfolioListWrapper portfolioListWrapper) {

        for (PortfolioItem item : portfolioListWrapper.getPortfolioItems()) {
            if (name.equals(item.getName())) {
                return false;
            }
        }

        return true;
    }

}
