package com.dritan.stockdata;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class StockForm {

    @EJB
    private StockSupplier supplier;
    private Stock stock;

    public void onFetchData() {
        stock = supplier.get();
    }

    public boolean isRenderable() {
        return stock != null;
    }

    public Stock getStock() {
        return stock;
    }
}