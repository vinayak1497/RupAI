package com.example.finvoice;

import static java.util.Arrays.asList;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView tvQuestion, tvOption1, tvOption2, tvOption3, tvOption4;
    private CardView cardOption1, cardOption2, cardOption3, cardOption4;
    private ProgressBar progressBarTimer;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;

    private List<Question> questions = asList(
            new Question("What is the full form of EMI?",
                    Arrays.asList("Equated Monthly Installment", "Easy Money Investment", "Early Mortgage Installment", "Enhanced Monthly Interest"), 0),

            new Question("Which investment option generally offers the highest returns over a long period?",
                    Arrays.asList("Fixed Deposit", "Stock Market", "Savings Account", "Recurring Deposit"), 1),

            new Question("What does the term 'inflation' refer to?",
                    Arrays.asList("Increase in the value of money", "Decrease in prices of goods", "Increase in the price of goods and services", "Increase in salaries"), 2),

            new Question("Which of the following is NOT a type of tax in India?",
                    Arrays.asList("Income Tax", "Goods and Services Tax (GST)", "Wealth Tax", "Luxury Tax"), 2),

            new Question("What is a mutual fund?",
                    Arrays.asList("A savings account with a high interest rate", "A pool of money from multiple investors managed by professionals", "A type of stock that guarantees fixed returns", "A government-issued bond"), 1),

            new Question("What is the main benefit of diversifying investments?",
                    Arrays.asList("Higher risk, higher return", "Lowering risk by spreading investments across different assets", "Investing only in stocks", "Getting tax benefits"), 1),

            new Question("Which organization regulates the stock market in India?",
                    Arrays.asList("RBI", "SEBI", "IRDA", "NSE"), 1),

            new Question("What is the minimum age required to open a bank savings account in India?",
                    Arrays.asList("10 years", "12 years", "15 years", "18 years"), 0),

            new Question("Which financial instrument is considered the safest for investment?",
                    Arrays.asList("Stock Market", "Fixed Deposit", "Gold", "Real Estate"), 1),

            new Question("What is the purpose of an emergency fund?",
                    Arrays.asList("To invest in high-risk stocks", "To cover unexpected expenses like medical bills or job loss", "To pay off loans faster", "To buy luxury items"), 1)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        cardOption1 = findViewById(R.id.cardOption1);
        tvOption1 = findViewById(R.id.tvOption1);
        cardOption2 = findViewById(R.id.cardOption2);
        tvOption2 = findViewById(R.id.tvOption2);
        cardOption3 = findViewById(R.id.cardOption3);
        tvOption3 = findViewById(R.id.tvOption3);
        cardOption4 = findViewById(R.id.cardOption4);
        tvOption4 = findViewById(R.id.tvOption4);
        progressBarTimer = findViewById(R.id.progressBarTimer);

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
            return;
        }

        Question question = questions.get(currentQuestionIndex);

        // Apply fade-out animation before changing question
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        tvQuestion.startAnimation(fadeOut);
        cardOption1.startAnimation(fadeOut);
        cardOption2.startAnimation(fadeOut);
        cardOption3.startAnimation(fadeOut);
        cardOption4.startAnimation(fadeOut);

        new Handler().postDelayed(() -> {
            tvQuestion.setText(question.getQuestion());
            List<String> options = question.getOptions();
            tvOption1.setText(options.get(0));
            tvOption2.setText(options.get(1));
            tvOption3.setText(options.get(2));
            tvOption4.setText(options.get(3));

            cardOption1.setOnClickListener(v -> checkAnswer(0, question.getCorrectAnswerIndex(), cardOption1));
            cardOption2.setOnClickListener(v -> checkAnswer(1, question.getCorrectAnswerIndex(), cardOption2));
            cardOption3.setOnClickListener(v -> checkAnswer(2, question.getCorrectAnswerIndex(), cardOption3));
            cardOption4.setOnClickListener(v -> checkAnswer(3, question.getCorrectAnswerIndex(), cardOption4));

            // Apply fade-in animation after changing question
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            tvQuestion.startAnimation(fadeIn);
            cardOption1.startAnimation(fadeIn);
            cardOption2.startAnimation(fadeIn);
            cardOption3.startAnimation(fadeIn);
            cardOption4.startAnimation(fadeIn);

            startTimer();
        }, 500); // Delay for fade-out effect
    }

    private void checkAnswer(int selectedIndex, int correctIndex, CardView selectedCard) {
        // Apply scale animation when option is clicked
        Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale);
        selectedCard.startAnimation(scaleAnim);

        // Stop the previous timer to reset time for the next question
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Highlight selected option
        if (selectedIndex == correctIndex) {
            selectedCard.setCardBackgroundColor(getResources().getColor(R.color.green));
            score++;
        } else {
            selectedCard.setCardBackgroundColor(getResources().getColor(R.color.red));
        }

        // Delay before loading next question
        new Handler().postDelayed(() -> {
            // Reset background color
            selectedCard.setCardBackgroundColor(getResources().getColor(R.color.white));

            // Move to the next question
            currentQuestionIndex++;
            loadNextQuestion();
        }, 1000);
    }

    private void startTimer() {
        // Cancel any existing timer before starting a new one
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Reset progress bar to 30 seconds
        progressBarTimer.setProgress(30);
        progressBarTimer.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

        // Start new countdown timer
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBarTimer.setProgress((int) (millisUntilFinished / 1000));

                if (millisUntilFinished < 10000) {
                    progressBarTimer.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onFinish() {
                currentQuestionIndex++;
                loadNextQuestion();
            }
        }.start();
    }
}
