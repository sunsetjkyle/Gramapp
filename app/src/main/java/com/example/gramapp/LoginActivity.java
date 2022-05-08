package com.example.gramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth lAuth;
    private FirebaseFirestore lFirestore;
    private EditText  lEmail, lPassword;
    private MaterialButton btn_login;
    private TextView sign_up, forgot;

    private Boolean emailAddressChecker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialise firebase authentication and Firestore

        lAuth = FirebaseAuth.getInstance();
        lFirestore = FirebaseFirestore.getInstance();

        //initialise the password and the email views

        lEmail = findViewById(R.id.login_email);
        lPassword = findViewById(R.id.login_password);

//        initialise the textviews

        sign_up = findViewById(R.id.txt_signup);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
        forgot = findViewById(R.id.txt_forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Password_resetActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });

//        initialise the button

        btn_login = findViewById(R.id.login);

//        give the button an Onclick listener

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                convert the texts to string
                String email = lEmail.getText().toString();
                String password = lPassword.getText().toString();

//                set an error if the Edit Texts are empty
                if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {

                    Toast.makeText(LoginActivity.this, "Cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (password.length()<6){
                    Toast.makeText(LoginActivity.this, "Try something longer!", Toast.LENGTH_SHORT).show();
                    
                }
                else {
                    signInUser(email, password);
                }
            }
        });

    }

    private void signInUser(String email, String password) {

        lAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                verifyEmailAddress();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Failed to sign in, Try again!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void verifyEmailAddress() {
        FirebaseUser user = lAuth.getCurrentUser();
        emailAddressChecker = user.isEmailVerified();

        if (emailAddressChecker){
            Toast.makeText(this, "Your Email is already verified, proceed to use", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = lAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }

    }
}