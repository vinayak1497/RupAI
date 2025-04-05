package com.example.finvoice;
public class StockDisplayItem {
    private final String date;
    private final double close;

    public StockDisplayItem(String date, double close) {
        this.date = date;
        this.close = close;
    }

    public String getDate() { return date; }
    public double getClose() { return close; }
}
