package com.example.finvoice;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.*;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockMarketActivity extends AppCompatActivity {

    private EditText stockSymbol;
    private Button fetchStockButton;
    private RecyclerView stockRecyclerView;
    private TextView trendText;

    private StockAdapter stockAdapter;
    private List<StockDisplayItem> displayList = new ArrayList<>();
    private final String API_KEY = "7MWS339RYQWH2N57"; // Replace with your API key
    private final String BASE_URL = "https://www.alphavantage.co/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_market);

        stockSymbol = findViewById(R.id.stockSymbol);
        fetchStockButton = findViewById(R.id.fetchStockButton);
        stockRecyclerView = findViewById(R.id.stockRecyclerView);
        trendText = findViewById(R.id.trendText);

        stockRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(displayList);
        stockRecyclerView.setAdapter(stockAdapter);

        fetchStockButton.setOnClickListener(v -> {
            String symbol = stockSymbol.getText().toString().trim();
            if (!symbol.isEmpty()) fetchStockData(symbol);
        });
    }

    private void fetchStockData(String symbol) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StockApi api = retrofit.create(StockApi.class);
        Call<JsonObject> call = api.getIntradayStockData(
                "TIME_SERIES_INTRADAY",
                symbol,
                "5min",             // required interval
                API_KEY,
                "compact"           // or "full"
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject json = response.body();
                    Log.d("STOCK_API", "Success: " + json.toString());

                    // Access nested time series object
                    JsonObject timeSeries = json.getAsJsonObject("Time Series (5min)");
                    if (timeSeries == null || timeSeries.entrySet().isEmpty()) {
                        Log.e("STOCK_API", "Missing time series data. Full body: " + json.toString());
                        Toast.makeText(StockMarketActivity.this, "No stock data available. Check symbol or try again later.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    displayList.clear();
                    List<String> sortedDates = new ArrayList<>(timeSeries.keySet());
                    Collections.sort(sortedDates); // Oldest to newest

                    for (String date : sortedDates.subList(Math.max(sortedDates.size() - 7, 0), sortedDates.size())) {
                        JsonObject data = timeSeries.getAsJsonObject(date);
                        double close = Double.parseDouble(data.get("4. close").getAsString());
                        displayList.add(new StockDisplayItem(date, close));
                    }

                    stockAdapter.notifyDataSetChanged();
                    displayTrend(displayList);
                } else {
                    Log.e("API_RESPONSE", "Body: " + response.errorBody());
                    Toast.makeText(StockMarketActivity.this, "Unexpected response from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(StockMarketActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTrend(List<StockDisplayItem> list) {
        if (list.size() < 2) {
            trendText.setText("Trend: Not enough data");
            return;
        }

        StringBuilder trendBuilder = new StringBuilder("📈 Trend (close prices):\n");
        for (StockDisplayItem item : list) {
            trendBuilder.append(item.getDate()).append(" → ₹").append(item.getClose()).append("\n");
        }

        double first = list.get(0).getClose();
        double last = list.get(list.size() - 1).getClose();
        String trend = last > first ? "📈 Upward Trend" : last < first ? "📉 Downward Trend" : "⏸ Stable";

        trendBuilder.append("\n").append("Overall: ").append(trend);
        trendText.setText(trendBuilder.toString());
    }
}
