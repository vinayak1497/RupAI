package com.example.finvoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {
    private TextView tvScore, tvResultMessage;
    private Button btnRetry, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Bind Views
        tvScore = findViewById(R.id.tvScore);
        tvResultMessage = findViewById(R.id.tvResultMessage);
        btnRetry = findViewById(R.id.btnRetry);
        btnExit = findViewById(R.id.btnExit);

        // Get score from QuizActivity
        int score = getIntent().getIntExtra("score", -1);

        // Prevent incorrect scores
        if (score == -1) {
            tvScore.setText("Error: Score not found");
            tvResultMessage.setText("Something went wrong.");
        } else {
            tvScore.setText("Your Score: " + score + "/10");

            // Show motivational messages
            if (score == 10) {
                tvResultMessage.setText("🏆 Perfect! You are a finance expert! 🎉");
            } else if (score >= 7) {
                tvResultMessage.setText("👏 Great Job! You have a good grasp of finance.");
            } else if (score >= 4) {
                tvResultMessage.setText("😊 Good effort! Keep learning.");
            } else {
                tvResultMessage.setText("📚 Don't worry! Keep practicing to improve.");
            }
        }

        // Retry Quiz
        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, StartQuiz.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Reset quiz
            startActivity(intent);
            finish();
        });

        // Exit App
        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, OnBoarding_Menu.class);// Reset quiz
            startActivity(intent);
            finish();
        }); // Closes all activities
    }
}
