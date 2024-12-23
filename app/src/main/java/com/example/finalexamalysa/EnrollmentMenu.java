package com.example.finalexamalysa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EnrollmentMenu extends AppCompatActivity {
    private Button btnSelectSubjects;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_menu);

        btnSelectSubjects = findViewById(R.id.btnSelectSubjects);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            username = currentUser.getEmail();
        } else {

            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish(); // Close the current activity
            return;
        }

        btnSelectSubjects.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectSubject.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        String username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Failed to get username.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }
}
