package com.example.smavehyiashahid.equithon;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText emailEditText, passwordEditText;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emailEditText= findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progress_signup);

        Button activitiesButton = findViewById(R.id.activities);
        activitiesButton.setOnClickListener(this);


        TextView signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(this);


        Button guardianButton = findViewById(R.id.guardiansButton);
        guardianButton.setOnClickListener(this);




    }

    private void userLogin(View view) {
        Integer id = view.getId();
        String email = emailEditText.getText().toString().trim();
        String password=passwordEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required.");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()){
            passwordEditText.setError("Password is required.");
            passwordEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is not valid. Please enter valid email");
            emailEditText.requestFocus();
            return;
        }

        if(password.length()<6){
            passwordEditText.setError("Password should be at least 6 characters.");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        if (id==R.id.activities) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent activitiesIntent = new Intent(MainActivity.this, FrontScreenActivity.class);
                        activitiesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(activitiesIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (id==R.id.guardiansButton){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent activitiesIntent = new Intent(MainActivity.this, GuardiansPageActivity.class);
                        activitiesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(activitiesIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                break;
            case R.id.activities:
                userLogin(v);
                break;

            case R.id.guardiansButton:
                userLogin(v);
                break;
        }
    }

    }



