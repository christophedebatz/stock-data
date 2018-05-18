package com.dritan.stockdata;

import javax.ejb.Local;
import java.util.function.Function;

@Local
public interface StockSupplier extends Function<String, Stock> {

}
