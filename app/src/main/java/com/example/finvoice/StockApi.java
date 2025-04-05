package com.example.finvoice;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockApi {
    @GET("query")
    Call<JsonObject> getIntradayStockData(
            @Query("function") String function,
            @Query("symbol") String symbol,
            @Query("interval") String interval,
            @Query("apikey") String apiKey,
            @Query("outputsize") String outputSize
    );
}
