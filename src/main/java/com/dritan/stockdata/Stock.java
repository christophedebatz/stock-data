package com.dritan.stockdata;

import java.util.Date;

public class Stock {

    private String code;
    private Date date;
    private double value;

    public Stock(String code, Date date, double value) {
        this.code = code;
        this.date = date;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }
}
