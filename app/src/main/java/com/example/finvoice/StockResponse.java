package com.example.finvoice;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class StockResponse {
    @SerializedName("Time Series (Daily)")
    private Map<String, StockData> timeSeries;

    public Map<String, StockData> getTimeSeries() {
        return timeSeries;
    }
}

