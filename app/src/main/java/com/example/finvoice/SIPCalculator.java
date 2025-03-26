package com.example.finvoice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SIPCalculator extends AppCompatActivity {

    private EditText investmentAmount, interestRate;
    private SeekBar yearSeekBar;
    private TextView yearLabel;
    private Button calculateButton;
    private TextView sipInvestment, sipTotalReturn, sipProfit, fdInvestment, fdTotalReturn, fdProfit, resultTitle;
    private LinearLayout resultTable;

    private static final double FD_INTEREST_RATE = 7.5; // FD Annual Rate
    private int selectedYears = 1; // Default investment duration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sipcalculator);

        // Initialize UI elements
        investmentAmount = findViewById(R.id.investmentAmount);
        interestRate = findViewById(R.id.interestRate);
        yearSeekBar = findViewById(R.id.yearSeekBar);
        yearLabel = findViewById(R.id.yearLabel);
        calculateButton = findViewById(R.id.calculateButton);
        resultTitle = findViewById(R.id.resultTitle);
        resultTable = findViewById(R.id.resultTable);

        sipInvestment = findViewById(R.id.sipInvestment);
        sipTotalReturn = findViewById(R.id.sipTotalReturn);
        sipProfit = findViewById(R.id.sipProfit);
        fdInvestment = findViewById(R.id.fdInvestment);
        fdTotalReturn = findViewById(R.id.fdTotalReturn);
        fdProfit = findViewById(R.id.fdProfit);

        // SeekBar change listener
        yearSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedYears = Math.max(progress, 1); // Minimum 1 year
                yearLabel.setText("Investment Duration: " + selectedYears + " Year" + (selectedYears > 1 ? "s" : ""));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Set initial label
        yearLabel.setText("Investment Duration: 1 Year");

        // Calculate button listener
        calculateButton.setOnClickListener(v -> compareSIPvsFD());
    }

    private void compareSIPvsFD() {
        String investmentStr = investmentAmount.getText().toString().trim();
        String rateStr = interestRate.getText().toString().trim();

        if (investmentStr.isEmpty() || rateStr.isEmpty()) {
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
            return;
        }

        double P = Double.parseDouble(investmentStr);
        double sipRate = Double.parseDouble(rateStr) / 12 / 100;
        int n = selectedYears * 12; // Total months
        double fdRate = FD_INTEREST_RATE / 100;

        // SIP Future Value Calculation
        double sipFutureValue = P * ((Math.pow((1 + sipRate), n) - 1) / sipRate) * (1 + sipRate);

        // FD Future Value Calculation (Compounded Annually)
        double fdFutureValue = P * 12 * ((Math.pow((1 + fdRate), n / 12)) - 1) / fdRate;

        sipInvestment.setText(String.format("₹%.2f", P * n));
        sipTotalReturn.setText(String.format("₹%.2f", sipFutureValue));
        sipProfit.setText(String.format("₹%.2f", sipFutureValue - (P * n)));

        fdInvestment.setText(String.format("₹%.2f", P * n));
        fdTotalReturn.setText(String.format("₹%.2f", fdFutureValue));
        fdProfit.setText(String.format("₹%.2f", fdFutureValue - (P * n)));

        resultTitle.setVisibility(View.VISIBLE);
        resultTable.setVisibility(View.VISIBLE);
    }
}
