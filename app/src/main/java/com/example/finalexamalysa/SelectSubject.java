package com.example.finalexamalysa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSubject extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout subjectContainer;
    private List<Map<String, Object>> selectedSubjects = new ArrayList<>();
    private int totalCredits = 0;
    private String username; // Untuk username yang diterima dari Login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        // Terima username dari Login
        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not found. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        subjectContainer = findViewById(R.id.subjectContainer);

        // Fetch data dari Firestore
        fetchSubjectsFromFirestore();

        Button btnSaveSubjects = findViewById(R.id.btnSaveSubjects);
        btnSaveSubjects.setOnClickListener(v -> saveSelectedSubjects());
    }

    private void fetchSubjectsFromFirestore() {
        db.collection("subject").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String subjectName = document.getString("name");
                            int credits = Integer.parseInt(document.getString("credits"));
                            addSubjectToUI(subjectName, credits, document.getId());
                        }
                    } else {
                        Log.w("SelectSubject", "Error getting documents.", task.getException());
                        Toast.makeText(this, "Failed to fetch subjects.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSubjectToUI(String subjectName, int credits, String subjectId) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(subjectName + " (" + credits + " credits)");
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Map<String, Object> subject = new HashMap<>();
                subject.put("subject_id", subjectId);
                subject.put("subject_name", subjectName);
                subject.put("credits", credits);
                selectedSubjects.add(subject);
                totalCredits += credits;
            } else {
                selectedSubjects.removeIf(sub -> sub.get("subject_id").equals(subjectId));
                totalCredits -= credits;
            }
        });
        subjectContainer.addView(checkBox);
    }

    private void saveSelectedSubjects() {
        if (selectedSubjects.isEmpty()) {
            Toast.makeText(this, "Please select at least one subject.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, EnrollmentSummary.class);
        intent.putExtra("username", username); // Kirim username dari Login
        intent.putExtra("totalCredits", totalCredits);
        intent.putExtra("subjects", (ArrayList<Map<String, Object>>) selectedSubjects);
        startActivity(intent);
    }
}
