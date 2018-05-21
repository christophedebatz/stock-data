package com.dritan.stockdata;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Stateless
public class DaxStockSupplier implements StockSupplier {

    private final String DAX_CRAWL_URL =
            "https://investir.lesechos.fr/cours/indice-dax-xetra,xetr,dax,de0008469008,isin.html";

    @Override
    public Stock get() {
        try {
            final Document document = Jsoup.connect(DAX_CRAWL_URL).get();
            final Double price = parsePrice(document);
            final Date date = parseDate(document);
            return new Stock("DAX", date, price);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Double parsePrice(final Document document) {
        final Elements elements = document.getElementsByAttributeValue("data-field", "valorisationHeaderFiche");
        if (elements.size() > 0) {
            final String text = elements.get(0).ownText();
            if (text != null && !text.isEmpty()) {
                return Double.valueOf(
                        text.replace(" ", "")
                                .replace(",", ".")
                );
            }
        }
        return null;
    }

    private Date parseDate(final Document document) throws ParseException {
        DateTime date = DateTime.now();
        final Elements dayElements = document.getElementsByAttributeValue("data-field", "jourHeaderFiche");
        if (dayElements.size() > 0) {
            final String dayText = dayElements.get(0).ownText();
            if (dayText != null && !dayText.isEmpty()) {
                date = DateTime.parse(dayText, DateTimeFormat.forPattern("dd/MM/yy"));
            }
        }
        final Elements timeElements = document.getElementsByAttributeValue("data-field", "heureSecondesHeaderFiche");
        if (timeElements.size() > 0) {
            final String timeText = timeElements.get(0).ownText();
            if (timeText != null && !timeText.isEmpty()) {
                List<String> parts = Arrays.asList(timeText.split("\\s"));
                final DateTime time = parseDateParts(parts);
                return new DateTime(date)
                        .withTime(
                            time.getHourOfDay(),
                            time.getMinuteOfHour(),
                            time.getSecondOfMinute(),
                            time.getMillisOfSecond()
                        )
                    .toDate();
            }
        }
        return date.toDate();
    }

    private DateTime parseDateParts(final List<String> parts) {
        return DateTime.parse(
                String.format("%d:%d:%d",
                        Integer.decode(parts.get(0).substring(0, 2)),
                        Integer.decode(parts.get(1).substring(0, 2)),
                        Integer.decode(parts.get(2).substring(0, 2))
                ),
                DateTimeFormat.forPattern("HH:mm:ss")
        );
    }
}
