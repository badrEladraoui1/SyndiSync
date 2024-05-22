package com.example.syndisync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reclamations extends AppCompatActivity {

    private EditText reclamationDescription;
    private Button submitReclamationButton;
    private List<CharSequence> reclamationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReclamationAdapter adapter; // This is a custom adapter that you need to create

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamations);

        // Initialize the RecyclerView and the adapter
        recyclerView = findViewById(R.id.reclamation_recycler_view);
        adapter = new ReclamationAdapter(reclamationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        reclamationDescription = findViewById(R.id.reclamation_description);
        submitReclamationButton = findViewById(R.id.submit_reclamation_button);

        submitReclamationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReclamation();
            }
        });

        getReclamations();
    }

    private void submitReclamation() {
        String description = reclamationDescription.getText().toString();

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        String ownerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // get the owner's email from Firebase Authentication

        Map<String, Object> reclamation = new HashMap<>();
        reclamation.put("description", description);
        reclamation.put("ownerEmail", ownerEmail); // use the owner's email

        db.collection("reclamations")
                .add(reclamation)
                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Reclamation submitted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding reclamation", Toast.LENGTH_SHORT).show());
    }

    private void getReclamations() {
        db.collection("reclamations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reclamationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");
                            String ownerEmail = document.getString("ownerEmail");
                            String text = "Reclamation from: " + ownerEmail + "\n\nThe issue is: " + description;
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

                            // Bold and italicize the owner's email
                            int startEmail = text.indexOf(ownerEmail);
                            int endEmail = startEmail + ownerEmail.length();
                            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startEmail, endEmail, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            // Bold the issue content
                            int startIssue = text.indexOf(description);
                            int endIssue = startIssue + description.length();
                            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startIssue, endIssue, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            reclamationList.add(spannableStringBuilder);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error getting reclamations", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}