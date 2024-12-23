package com.example.finalexamalysa;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class EnrollmentSummary extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        db = FirebaseFirestore.getInstance();

        String username = getIntent().getStringExtra("username");
        int totalCredits = getIntent().getIntExtra("totalCredits", 0);
        List<Map<String, Object>> subjects = (List<Map<String, Object>>) getIntent().getSerializableExtra("subjects");

        if (username == null || subjects == null) {
            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("EnrollmentSummary", "Username: " + username);
        Log.d("EnrollmentSummary", "Total Credits: " + totalCredits);
        Log.d("EnrollmentSummary", "Subjects: " + subjects);

        TextView txtSelectedSubjects = findViewById(R.id.txtSelectedSubjects);
        TextView txtTotalCredits = findViewById(R.id.txtTotalCredits);

        StringBuilder subjectsList = new StringBuilder();
        for (Map<String, Object> subject : subjects) {
            String subjectName = (String) subject.get("subject_name");
            int credits = ((Number) subject.get("credits")).intValue();
            subjectsList.append("- ").append(subjectName).append(" (").append(credits).append(" credits)\n");
        }

        txtSelectedSubjects.setText("Selected Subjects:\n" + subjectsList.toString());
        txtTotalCredits.setText("Total Credits: " + totalCredits);

        saveDataToFirestore(username, totalCredits, subjects);
    }

    private void saveDataToFirestore(String username, int totalCredits, List<Map<String, Object>> subjects) {
        Map<String, Object> enrollmentData = Map.of(
                "username", username,
                "totalCredits", totalCredits,
                "selectedSubjects", subjects
        );

        db.collection("enrollments")
                .document(username)
                .set(enrollmentData)
                .addOnSuccessListener(aVoid -> Log.d("EnrollmentSummary", "Data saved successfully for " + username))
                .addOnFailureListener(e -> Log.e("EnrollmentSummary", "Error saving data", e));
    }
}
