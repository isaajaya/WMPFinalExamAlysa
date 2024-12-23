package com.example.finalexamalysa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> validateAndLogin());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, Register.class)));
    }

    private void validateAndLogin() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        Toast.makeText(Login.this, "Welcome, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, SelectSubject.class);
                        intent.putExtra("username", user.getEmail()); // Send email to SelectSubject
                        startActivity(intent);
                        finish();
                    }
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed.";
                    Toast.makeText(Login.this, "Incorrect email or password: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
