package com.example.finvoice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnBoarding_Menu extends AppCompatActivity {
    private CardView SIP_Calculator, chatBot, budget, quiz;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SIP_Calculator = findViewById(R.id.SIP_Calculator);
        SIP_Calculator.setOnClickListener(view -> {
            Intent intent = new Intent(OnBoarding_Menu.this, SIPCalculator.class);
            startActivity(intent);
        });

        chatBot = findViewById(R.id.Chatbot);
        chatBot.setOnClickListener(view -> {
            Intent intent = new Intent(OnBoarding_Menu.this, AiChatBot.class);
            startActivity(intent);
        });

        budget = findViewById(R.id.Budget_Planner);
        budget.setOnClickListener(view -> {
            Intent intent = new Intent(OnBoarding_Menu.this, BudgetPlannerActivity.class);
            startActivity(intent);
        });

        quiz = findViewById(R.id.quizs);
        quiz.setOnClickListener(view -> {
            Intent intent = new Intent(OnBoarding_Menu.this, StartQuiz.class);
            startActivity(intent);
        });

    }
}