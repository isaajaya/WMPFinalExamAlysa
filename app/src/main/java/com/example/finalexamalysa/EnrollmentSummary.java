package com.example.finalexamalysa;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class EnrollmentSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        // Ambil data dari Intent
        String username = getIntent().getStringExtra("username");
        int totalCredits = getIntent().getIntExtra("totalCredits", 0);
        List<Map<String, Object>> subjects = (List<Map<String, Object>>) getIntent().getSerializableExtra("subjects");

        // Logging untuk debugging
        Log.d("EnrollmentSummary", "Username: " + username);
        Log.d("EnrollmentSummary", "Total Credits: " + totalCredits);
        Log.d("EnrollmentSummary", "Subjects: " + subjects);

        // Validasi data
        if (username == null || subjects == null) {
            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView txtSelectedSubjects = findViewById(R.id.txtSelectedSubjects);
        TextView txtTotalCredits = findViewById(R.id.txtTotalCredits);

        if (!subjects.isEmpty()) {
            StringBuilder subjectsList = new StringBuilder();
            for (Map<String, Object> subject : subjects) {
                String subjectName = (String) subject.get("subject_name");
                int credits = ((Number) subject.get("credits")).intValue();
                subjectsList.append("- ").append(subjectName).append(" (").append(credits).append(" credits)\n");
            }
            txtSelectedSubjects.setText("Selected Subjects:\n" + subjectsList.toString());
        } else {
            txtSelectedSubjects.setText("Selected Subjects: None");
        }

        txtTotalCredits.setText("Total Credits: " + totalCredits);
    }
}