package vu.project.mobilestockstest.model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PortfolioListWrapper implements Serializable {

    private List<PortfolioItem> portfolioItems;

    public List<PortfolioItem> getPortfolioItems() {
        return portfolioItems;
    }

    public PortfolioListWrapper() {
        this.portfolioItems = new ArrayList<>();
    }

    public void save(String fileName, Context context) {
        FileOutputStream fos;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();

        } catch (FileNotFoundException e) {
            System.out.println("error");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }

    public static PortfolioListWrapper load(String fileName, Context context){
        PortfolioListWrapper item = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            item = (PortfolioListWrapper) is.readObject();
            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return item;
    }


}
