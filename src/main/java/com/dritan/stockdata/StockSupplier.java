package com.dritan.stockdata;

import javax.ejb.Local;
import java.util.function.Function;
import java.util.function.Supplier;

@Local
interface StockSupplier extends Supplier<Stock> {

}
