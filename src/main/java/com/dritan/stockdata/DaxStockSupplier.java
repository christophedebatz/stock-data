package com.dritan.stockdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class DaxStockSupplier implements StockSupplier {

    private final String AV_API_KEY = "X3P0HSFSYX0VD4AQ";
    private final String AV_API_ENDPOINT = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=1min&apikey=%s";
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Stock apply(final String code) {
        if (code != null && !code.isEmpty()) {
            final String url = buildUrl(code);
            final String json = fetch(url);
            try {
                return getStockFromJson(code, json);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private String buildUrl(final String code) {
        return String.format(AV_API_ENDPOINT, code, AV_API_KEY);
    }

    private Stock getStockFromJson(final String code, final String json) throws ParseException {
        if (json == null) {
            return null;
        }
        JsonParser parser = new JsonParser();
        final JsonObject object = parser.parse(json).getAsJsonObject();
        if (object.has("Time Series (1min)")) {
            final JsonObject timeSeries = object.getAsJsonObject("Time Series (1min)");
            final Iterator<Map.Entry<String, JsonElement>> iterator = timeSeries.entrySet().iterator();
            if (iterator.hasNext()) {
                final Map.Entry<String, JsonElement> next = iterator.next();
                final Date date = DATE_FORMAT.parse(next.getKey());
                final Double value = next.getValue().getAsJsonObject().get("1. open").getAsDouble();
                return new Stock(code, date, value);
            }
        }
        return null;
    }

    private String fetch(final String url) {
        try {
            final URL endpointUrl = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod("GET");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            return jsonBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
