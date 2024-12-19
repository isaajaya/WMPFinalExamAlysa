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

        // Get the currently logged-in user from FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            username = currentUser.getEmail(); // Get the email of the logged-in user
        } else {
            // If no user is logged in, navigate back to the login screen
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
    }
}
