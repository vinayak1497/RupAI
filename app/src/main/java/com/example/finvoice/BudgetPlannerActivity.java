package com.example.finvoice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.TextPart;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class BudgetPlannerActivity extends AppCompatActivity {

    private EditText etIncome, etOtherIncome, etMedicalExpenses, etGroceries, etRent, etEducation, etSavings;
    private Button btnGenerateBudget;
    private TextView tvBudgetResult;

    private static final String MODEL_NAME = "gemini-2.0-flash";  // Use the same model as in ChatBot
    private static final String API_KEY = "AIzaSyCzNfaMSGCvoCVjrFdUo2fAV8yXNK9WK-g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_planner);

        etIncome = findViewById(R.id.etIncome);
        etOtherIncome = findViewById(R.id.etOtherIncome);
        etMedicalExpenses = findViewById(R.id.etMedicalExpenses);
        etGroceries = findViewById(R.id.etGroceries);
        etRent = findViewById(R.id.etRent);
        etEducation = findViewById(R.id.etEducation);
        etSavings = findViewById(R.id.etSavings);
        btnGenerateBudget = findViewById(R.id.btnGenerateBudget);
        tvBudgetResult = findViewById(R.id.tvBudgetResult);

        btnGenerateBudget.setOnClickListener(v -> generateBudgetPlan());
    }

    private void generateBudgetPlan() {
        // 📝 Gather user input
        String income = etIncome.getText().toString().trim();
        String otherIncome = etOtherIncome.getText().toString().trim();
        String medical = etMedicalExpenses.getText().toString().trim();
        String groceries = etGroceries.getText().toString().trim();
        String rent = etRent.getText().toString().trim();
        String education = etEducation.getText().toString().trim();
        String savings = etSavings.getText().toString().trim();

        if (income.isEmpty() || savings.isEmpty()) {
            tvBudgetResult.setVisibility(View.VISIBLE);
            tvBudgetResult.setText("⚠️ Please enter Income and Savings to generate a budget plan.");
            return;
        }

        // 🔹 Create structured prompt for better AI response
        String prompt = "User Financial Data:\n"
                + "Total Income after Tax: ₹" + income + "\n"
                + "Other Income Sources: ₹" + otherIncome + "\n"
                + "Medical Expenses: ₹" + medical + "\n"
                + "Groceries: ₹" + groceries + "\n"
                + "Rent/Mortgage/EMI: ₹" + rent + "\n"
                + "Education Fees & Insurance: ₹" + education + "\n"
                + "Monthly Savings: ₹" + savings + "\n\n"
                + "💰 **Task:** Based on the user's financial details, generate a personalized budget plan. "
                + "Ensure the response is structured with key financial insights and savings strategies. Give reply with simple language ensuring that the user is from rural community. Don't use bold and italic in response. Keep response simple and short. ";

        // 🔹 Call Gemini AI
        callGeminiAPI(prompt);
    }

    private void callGeminiAPI(String prompt) {
        GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);

        model.generateContent(prompt, new Continuation<GenerateContentResponse>() {
            @Override
            public void resumeWith(Object result) {
                if (result instanceof GenerateContentResponse) {
                    Content content = ((GenerateContentResponse) result).getCandidates().get(0).getContent();
                    if (content != null) {
                        String responseText = ((TextPart) content.getParts().get(0)).getText();

                        runOnUiThread(() -> {
                            tvBudgetResult.setVisibility(View.VISIBLE);
                            tvBudgetResult.setText(responseText);
                        });
                    }
                } else if (result instanceof Throwable) {
                    Log.e("GeminiAPI", "Error generating response", (Throwable) result);
                    runOnUiThread(() -> {
                        tvBudgetResult.setVisibility(View.VISIBLE);
                        tvBudgetResult.setText("❌ Unable to generate a budget plan. Please try again later.");
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
