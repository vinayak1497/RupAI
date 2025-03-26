package com.example.finvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finvoice.HelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginMoodle, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginMoodle = findViewById(R.id.login_moodle);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()){
                    Toast.makeText(LoginActivity.this, "Please enter right details.", Toast.LENGTH_SHORT).show();
                } else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername(){
        String val = loginMoodle.getText().toString();
        if (val.isEmpty()){
            loginMoodle.setError("Username cannot be empty");
            return false;
        } else {
            loginMoodle.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginMoodle.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userUsername);

        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot userSnapshot = task.getResult();

                String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {

                    // Retrieve user details
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String contactNo = userSnapshot.child("contactNo").getValue(String.class);
                    String branch = userSnapshot.child("branch").getValue(String.class);
                    String division = userSnapshot.child("division").getValue(String.class);
                    String year = userSnapshot.child("year").getValue(String.class);

                    // Store data in Singleton
                    HelperClass user = new HelperClass(nameFromDB, emailFromDB, userUsername, passwordFromDB, contactNo);
                    UserDataSingleton.getInstance().setUserData(user);

                    // Navigate to main fragment
                    Intent intent = new Intent(LoginActivity.this, OnBoarding_Menu.class);
                    startActivity(intent);
                } else {
                    loginPassword.setError("Invalid Credentials");
                    loginPassword.requestFocus();
                }
            } else {
                loginMoodle.setError("User does not exist");
                loginMoodle.requestFocus();
            }
        });
    }
}