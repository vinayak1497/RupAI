package com.example.finvoice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnBoarding_Menu extends AppCompatActivity {
    private CardView SIP_Calculator, chatBot, budget, quiz, saving, stock;
    private TextView userNameTextView;
    private String name;

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

        // Find the TextView
        userNameTextView = findViewById(R.id.userNameTextView);

        // Retrieve name from intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        // Set the name if it's not null
        if (name != null && !name.isEmpty()) {
            userNameTextView.setText("Hey, " + name + "!");
        } else {
            userNameTextView.setText("Hey, User!");
        }

        // Setup CardView click listeners
        SIP_Calculator = findViewById(R.id.SIP_Calculator);
        SIP_Calculator.setOnClickListener(view -> {
            Intent sipIntent = new Intent(OnBoarding_Menu.this, SIPCalculator.class);
            startActivity(sipIntent);
        });

        chatBot = findViewById(R.id.Chatbot);
        chatBot.setOnClickListener(view -> {
            Intent chatbotIntent = new Intent(OnBoarding_Menu.this, AiChatBot.class);
            startActivity(chatbotIntent);
        });

        budget = findViewById(R.id.Budget_Planner);
        budget.setOnClickListener(view -> {
            Intent budgetIntent = new Intent(OnBoarding_Menu.this, BudgetPlannerActivity.class);
            startActivity(budgetIntent);
        });

        quiz = findViewById(R.id.quizs);
        quiz.setOnClickListener(view -> {
            Intent quizIntent = new Intent(OnBoarding_Menu.this, StartQuiz.class);
            startActivity(quizIntent);
        });

        saving = findViewById(R.id.savingsTracker);
        saving.setOnClickListener(view -> {
            Intent quizIntent = new Intent(OnBoarding_Menu.this, PurchaseAnalyzerActivity.class);
            startActivity(quizIntent);
        });

        stock = findViewById(R.id.stocks);
        stock.setOnClickListener(view -> {
            Intent quizIntent = new Intent(OnBoarding_Menu.this, StockMarketActivity.class);
            startActivity(quizIntent);
        });


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        findViewById(R.id.main).startAnimation(fadeIn);
    }
}