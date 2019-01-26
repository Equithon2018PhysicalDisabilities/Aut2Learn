package com.example.smavehyiashahid.equithon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editUserEmail, editPassword, editFirstName, editLastName, editConfirmPass;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress_signup);

        editUserEmail = findViewById(R.id.email_regis);
        editPassword = findViewById(R.id.pass1_regis);
        editConfirmPass = findViewById(R.id.pass2_regis);
        editFirstName= findViewById(R.id.firstname_regis);
        editLastName = findViewById(R.id.lastname_regis);

        findViewById(R.id.signUpaccount).setOnClickListener(this);
        findViewById(R.id.loginFromSignup).setOnClickListener(this);
    }
    private void registerUser(){
        String email = editUserEmail.getText().toString().trim();
        String password= editPassword.getText().toString().trim();
        String firstname= editFirstName.getText().toString().trim();
        String lastname= editLastName.getText().toString().trim();
        String confirmPassword= editConfirmPass.getText().toString().trim();
        // validate inputs
        String[] fields = {email,password,firstname,lastname,confirmPassword};
        TextView[] editFields= {editUserEmail, editPassword, editFirstName, editLastName,editConfirmPass};
        String[] requiredFields = {"Email ", "Password ", "First Name ", "Last Name ","Confirm Password "};

        for (int i=0; i<fields.length; i++) {
            if(fields[i].isEmpty()){
                editFields[i].setError(requiredFields[i]+"is required");
                return;
            }

        }

        if (!password.equals(confirmPassword)){
            editPassword.setError("Passwords do not match");
            editConfirmPass.setError("Passwords do not match");
            editPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editUserEmail.setError("Email is not valid. Please enter valid email");
            editUserEmail.requestFocus();
            return;
        }

        if(password.length()<6){
            editPassword.setError("Password should be at least 6 characters.");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent activitiesIntent = new Intent(SignUpActivity.this, FrontScreenActivity.class);
                    activitiesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(activitiesIntent);
                    Toast.makeText(getApplicationContext(),"User Registration Successful!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User is already registered!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });


    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signUpaccount:
                registerUser();
                break;

            case R.id.loginFromSignup:
                startActivity(new Intent(this,MainActivity.class));
                break;

        }
    }
}
