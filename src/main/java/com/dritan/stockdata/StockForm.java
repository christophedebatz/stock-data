package com.dritan.stockdata;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class StockForm {

    @EJB
    private StockSupplier supplier;

    private String code;
    private Stock stock;

    public void onFetchData() {
        System.out.println("on fetch data code = " + code);
        stock = supplier.apply(code);
        if (stock != null) {
            System.out.println("value=" + stock.getValue());
        }
    }

    public boolean isRenderable() {
        return stock != null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Stock getStock() {
        if (stock != null) {
            System.out.println("get stock=" + stock.getValue());
        } else {
            System.out.println("no stock");
        }
        return stock;
    }
}