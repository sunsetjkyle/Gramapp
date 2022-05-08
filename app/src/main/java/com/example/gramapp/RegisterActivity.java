package com.example.gramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private MaterialButton register;
    private EditText rEmail, rPassword;
    private TextView  txt_account, text_login ;
    private FirebaseAuth rAuth;
    private FirebaseFirestore rFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);

        rEmail = findViewById(R.id.reg_email);
        rPassword = findViewById(R.id.reg_password);

        text_login = findViewById(R.id.reg_txt_login);
        txt_account = findViewById(R.id.account);

        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


        rAuth = FirebaseAuth.getInstance();
        rFirestore = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String register_email = rEmail.getText().toString();
                String register_password = rPassword.getText().toString();

                if (TextUtils.isEmpty(register_email)|| TextUtils.isEmpty(register_password)) {
                    Toast.makeText(RegisterActivity.this, "Cannot be Empty!", Toast.LENGTH_SHORT).show();

                }
                else if (register_password.length()<6) {
                    Toast.makeText(RegisterActivity.this, "Try something longer", Toast.LENGTH_SHORT).show();
                }

                else {
                    createNewUser(register_email, register_password);
                }


            }
        });

    }

    private void createNewUser(String register_email, String register_password) {

        rAuth.createUserWithEmailAndPassword(register_email, register_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Created new user succesfully!", Toast.LENGTH_SHORT).show();
                    sendVerificationEmail();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Failed, Try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(RegisterActivity.this, "Registration successful, please verify your account", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, StartupActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, "Failed to verify Email! Try again ", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}