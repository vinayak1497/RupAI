package com.example.finvoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StartQuiz extends AppCompatActivity {
    private TextView tvObjective, tvMotivation, tvInstructions;
    private Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        // Initialize Views
        tvObjective = findViewById(R.id.tvObjective);
        tvMotivation = findViewById(R.id.tvMotivation);
        tvInstructions = findViewById(R.id.tvInstructions);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        // Set the content
        tvObjective.setText("📌 Objective: Test your financial literacy with 10 quick questions.");
        tvMotivation.setText("💡 'An investment in knowledge pays the best interest.' - Benjamin Franklin");
        tvInstructions.setText("✔️ You have 30 seconds per question.\n✔️ Choose the correct option.\n✔️ Try to score as high as possible!");

        // Start Quiz when button is clicked
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(StartQuiz.this, QuizActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
