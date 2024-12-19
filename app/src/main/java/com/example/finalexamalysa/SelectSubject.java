package com.example.finalexamalysa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSubject extends AppCompatActivity {
    private CheckBox chkDatabaseSystem, chkNetworkSecurity, chkWirelessMobileProgramming,
            chkCalculus, chkComputerNetwork, chkDataStructureAlgorithm, chkDiscreteMathematics,
            chkLinearAlgebra, chkNumericalMethods, chkProbabilityStatistics,
            chkProgrammingConcepts, chkServerSideProgramming;
    private Button btnSaveSubjects;
    private static final int MAX_CREDITS = 24;
    private String username;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        username = getIntent().getStringExtra("username"); // Retrieve the email
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // End the activity if no username is passed
            return;
        }

        db = FirebaseFirestore.getInstance();

        // Inisialisasi checkbox dan button
        chkDatabaseSystem = findViewById(R.id.chkDatabaseSystem);
        chkNetworkSecurity = findViewById(R.id.chkNetworkSecurity);
        chkWirelessMobileProgramming = findViewById(R.id.chkWirelessMobileProgramming);
        chkCalculus = findViewById(R.id.chkCalculus);
        chkComputerNetwork = findViewById(R.id.chkComputerNetwork);
        chkDataStructureAlgorithm = findViewById(R.id.chkDataStructureAlgorithm);
        chkDiscreteMathematics = findViewById(R.id.chkDiscreteMathematics);
        chkLinearAlgebra = findViewById(R.id.chkLinearAlgebra);
        chkNumericalMethods = findViewById(R.id.chkNumericalMethods);
        chkProbabilityStatistics = findViewById(R.id.chkProbabilityStatistics);
        chkProgrammingConcepts = findViewById(R.id.chkProgrammingConcepts);
        chkServerSideProgramming = findViewById(R.id.chkServerSideProgramming);
        btnSaveSubjects = findViewById(R.id.btnSaveSubjects);

        btnSaveSubjects.setOnClickListener(v -> saveSelectedSubjects());
    }

    private void saveSelectedSubjects() {
        List<Map<String, Object>> selectedSubjects = new ArrayList<>();
        int totalCredits = 0;

        // Periksa setiap checkbox
        if (chkDatabaseSystem.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Database System", 3));
        }
        if (chkNetworkSecurity.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Network Security", 3));
        }
        if (chkWirelessMobileProgramming.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Wireless and Mobile Programming", 3));
        }
        if (chkCalculus.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Calculus", 3));
        }
        if (chkComputerNetwork.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Computer Network", 3));
        }
        if (chkDataStructureAlgorithm.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Data Structure and Algorithm", 3));
        }
        if (chkDiscreteMathematics.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Discrete Mathematics", 3));
        }
        if (chkLinearAlgebra.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Linear Algebra", 3));
        }
        if (chkNumericalMethods.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Numerical Methods", 3));
        }
        if (chkProbabilityStatistics.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Probability and Statistics", 3));
        }
        if (chkProgrammingConcepts.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Programming Concepts", 3));
        }
        if (chkServerSideProgramming.isChecked()) {
            totalCredits += 3;
            selectedSubjects.add(createSubject("Server-side Internet Programming", 3));
        }

        // Validasi total kredit
        if (totalCredits > MAX_CREDITS) {
            Toast.makeText(this, "Total credits cannot exceed " + MAX_CREDITS, Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedSubjects.isEmpty()) {
            Toast.makeText(this, "Please select at least one subject", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data for Firestore
        Map<String, Object> enrollmentData = new HashMap<>();
        enrollmentData.put("username", username);
        enrollmentData.put("totalCredits", totalCredits);
        enrollmentData.put("selectedSubjects", selectedSubjects);

        // Save to Firestore
        int finalTotalCredits = totalCredits;
        db.collection("enrollments")
                .add(enrollmentData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Subjects saved successfully!", Toast.LENGTH_SHORT).show();
                    navigateToSummary(finalTotalCredits, selectedSubjects);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save subjects: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error adding document", e);
                });
    }

    private void navigateToSummary(int totalCredits, List<Map<String, Object>> selectedSubjects) {
        Intent intent = new Intent(this, EnrollmentSummary.class);
        intent.putExtra("username", username);
        intent.putExtra("totalCredits", totalCredits);
        intent.putExtra("subjects", (ArrayList<Map<String, Object>>) selectedSubjects);
        startActivity(intent);
        finish();
    }

    private Map<String, Object> createSubject(String name, int credits) {
        Map<String, Object> subject = new HashMap<>();
        subject.put("subject_name", name);
        subject.put("credits", credits);
        return subject;
    }
}
