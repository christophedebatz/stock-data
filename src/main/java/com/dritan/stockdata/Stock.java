package com.dritan.stockdata;

import java.util.Date;

public class Stock {

    private String code;
    private Date date;
    private Double value;

    public Stock(final String code, final Date date, final Double value) {
        this.code = code;
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public Double getValue() {
        return value;
    }
}
