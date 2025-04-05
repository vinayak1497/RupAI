package com.example.finvoice;
import com.google.gson.annotations.SerializedName;

public class StockData {
    @SerializedName("4. close")
    private String close;

    public double getClose() {
        return Double.parseDouble(close);
    }
}
