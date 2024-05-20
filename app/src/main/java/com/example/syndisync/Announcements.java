package com.example.syndisync;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Announcements extends AppCompatActivity {

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        Button submitButton = findViewById(R.id.submitAnnouncement);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleInput = findViewById(R.id.announcementTitle);
                EditText contentInput = findViewById(R.id.announcementContent);

                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();

                // Get the current user's ID and username
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                String username = user.getEmail(); // This assumes that the username is stored as the email in Firebase Auth

                // Create a new object for the announcement
                Map<String, Object> announcement = new HashMap<>();
                announcement.put("title", title);
                announcement.put("content", content);
                announcement.put("owner", userId);
                announcement.put("username", username);

                // Push the announcement to the Firestore database
                mDatabase.collection("announcements").add(announcement);

                // Add the announcement to your layout
                LinearLayout announcementsLayout = findViewById(R.id.announcementsLayout);
                TextView newAnnouncement = new TextView(Announcements.this);
                newAnnouncement.setText(username + "\n" + title + "\n" + content);
                announcementsLayout.addView(newAnnouncement);

                // Clear the input fields
                titleInput.setText("");
                contentInput.setText("");
            }
        });

        // Read the announcements from the Firestore database
        mDatabase.collection("announcements").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the LinearLayout that will contain the announcements
                LinearLayout announcementsLayout = findViewById(R.id.announcementsLayout);

                // Clear the LinearLayout
                announcementsLayout.removeAllViews();

                // Loop through all the announcements
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the announcement details
                    Map<String, Object> announcement = document.getData();

                    // Create a new TextView for the announcement
                    TextView newAnnouncement = new TextView(Announcements.this);
                    newAnnouncement.setText(announcement.get("username") + "\n" + announcement.get("title") + "\n" + announcement.get("content"));

                    newAnnouncement.setTextColor(Color.BLACK);
                    newAnnouncement.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    newAnnouncement.setPadding(16, 16, 16, 16);
                    newAnnouncement.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    newAnnouncement.setBackgroundColor(Color.LTGRAY);
                    newAnnouncement.setTypeface(Typeface.DEFAULT_BOLD);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 10, 10);
                    newAnnouncement.setLayoutParams(layoutParams);

                    // Add the TextView to the LinearLayout
                    announcementsLayout.addView(newAnnouncement);
                }
            } else {
                // Handle possible errors.
            }
        });
    }
}