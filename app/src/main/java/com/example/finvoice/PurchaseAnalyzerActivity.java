package com.example.finvoice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.TextPart;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class PurchaseAnalyzerActivity extends AppCompatActivity {

    private EditText etIncome, etCost;
    private EditText etSavings, etMonthlyExpenses, etProductLifespan, product;
    private Spinner spUrgent;
    private Button btnAnalyze;
    private TextView tvAdviceResult;

    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String API_KEY = "AIzaSyCzNfaMSGCvoCVjrFdUo2fAV8yXNK9WK-g";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_analyzer);

        etIncome = findViewById(R.id.etIncome);
        etCost = findViewById(R.id.etCost);
        spUrgent = findViewById(R.id.spUrgency);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        tvAdviceResult = findViewById(R.id.tvAdviceResult);
        etSavings = findViewById(R.id.etSavings);
        etMonthlyExpenses = findViewById(R.id.etMonthlyExpenses);
        etProductLifespan = findViewById(R.id.etProductLifespan);
        product = findViewById(R.id.productDetail);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.importance_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUrgent.setAdapter(adapter);

        btnAnalyze.setOnClickListener(v -> analyzePurchase());
    }

    private void analyzePurchase() {
        String income = etIncome.getText().toString().trim();
        String cost = etCost.getText().toString().trim();
        String Urgency = spUrgent.getSelectedItem().toString();
        String savings = etSavings.getText().toString().trim();
        String expenses = etMonthlyExpenses.getText().toString().trim();
        String lifespan = etProductLifespan.getText().toString().trim();
        String productDet = product.getText().toString().trim();

        if (income.isEmpty() || cost.isEmpty()) {
            tvAdviceResult.setVisibility(View.VISIBLE);
            tvAdviceResult.setText("⚠️ Please enter both Income and Product Cost to analyze.");
            return;
        }

        String prompt = "📊 Purchase Decision Help:\n"
                + "Monthly Income: ₹" + income + "\n"
                + "Product: " + productDet + "\n"
                + "Product Cost: ₹" + cost + "\n"
                + "Savings Available: ₹" + savings + "\n"
                + "Monthly Expenses: ₹" + expenses + "\n"
                + "Product Lifespan (years): " + lifespan + "\n"
                + "Urgency: " + Urgency + "\n\n"
                + "Should the user go ahead with this purchase now, delay it, or skip it? Give helpful financial reasoning in simple terms and short, considering the fact user is from rural population. Make his decision easier, if needed provide alternative or give some examples for better choices. Dont use emojis, styling like bold, italic etc.";
        callGeminiAPI(prompt);
    }

    private void callGeminiAPI(String prompt) {
        GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);

        model.generateContent(prompt, new Continuation<GenerateContentResponse>() {
            @Override
            public void resumeWith(Object result) {
                if (result instanceof GenerateContentResponse) {
                    Content content = ((GenerateContentResponse) result).getCandidates().get(0).getContent();
                    if (content != null && !content.getParts().isEmpty()) {
                        String responseText = ((TextPart) content.getParts().get(0)).getText();

                        runOnUiThread(() -> {
                            tvAdviceResult.setVisibility(View.VISIBLE);
                            tvAdviceResult.setText(responseText);
                        });
                    }
                } else if (result instanceof Throwable) {
                    Log.e("GeminiAPI", "Error generating advice", (Throwable) result);
                    runOnUiThread(() -> {
                        tvAdviceResult.setVisibility(View.VISIBLE);
                        tvAdviceResult.setText("❌ Failed to get advice. Please try again later.");
                    });
                }
            }

            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
        });
    }
}
