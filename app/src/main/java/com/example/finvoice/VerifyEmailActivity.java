package com.example.finvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyEmailActivity extends AppCompatActivity {
    private TextView verificationMessage;
    private Button resendEmailButton, continueButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private Handler handler = new Handler();
    private Runnable emailCheckRunnable;

    private String name, email, username, password, contactNo; // User data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve user details
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        contactNo = intent.getStringExtra("contactNo");

        // Initialize UI elements
        verificationMessage = findViewById(R.id.verification_message);
        resendEmailButton = findViewById(R.id.resend_email_button);
        continueButton = findViewById(R.id.continue_button);

        // Set verification message
        verificationMessage.setText("A verification email has been sent to:\n" + email);

        // Check email verification status every 5 seconds
        emailCheckRunnable = new Runnable() {
            @Override
            public void run() {
                currentUser.reload().addOnCompleteListener(task -> {
                    if (currentUser.isEmailVerified()) {
                        saveUserToDatabase();  // Save user data
                        navigateToNextScreen(); // Redirect to home or next screen
                    } else {
                        handler.postDelayed(emailCheckRunnable, 5000); // Retry after 5 seconds
                    }
                });
            }
        };
        handler.post(emailCheckRunnable);

        // Resend verification email
        resendEmailButton.setOnClickListener(view -> {
            if (currentUser != null) {
                currentUser.sendEmailVerification().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(VerifyEmailActivity.this, "Verification email resent!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VerifyEmailActivity.this, "Failed to resend email", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Manually check verification
        continueButton.setOnClickListener(view -> {
            currentUser.reload().addOnCompleteListener(task -> {
                if (currentUser.isEmailVerified()) {
                    saveUserToDatabase();
                    navigateToNextScreen();
                } else {
                    Toast.makeText(VerifyEmailActivity.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Save user details to Firebase Database
    private void saveUserToDatabase() {
        if (username != null && !username.isEmpty()) {
            User user = new User(name, email, username, contactNo, password);
            reference.child(username).setValue(user); // ✅ Save using username as key
        } else {
            Log.e("VerifyEmailActivity", "Username is null or empty, cannot save to database.");
        }
    }

    // Navigate to home or next screen
    private void navigateToNextScreen() {
        Toast.makeText(this, "Email verified successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VerifyEmailActivity.this, OnBoarding_Menu.class);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(emailCheckRunnable); // Stop polling when activity is destroyed
    }

    // User model class
    public static class User {
        public String name, email, username, contactNo, password;

        public User() { } // Required empty constructor

        public User(String name, String email, String username, String contactNo, String password) {
            this.name = name;
            this.email = email;
            this.username = username;
            this.contactNo = contactNo;
            this.password = password;
        }
    }
}