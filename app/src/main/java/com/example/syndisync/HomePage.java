package com.example.syndisync;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomePage extends AppCompatActivity {

    LinearLayout cardMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "User: getCurrentUser" + user);

        //debugging
        if (user != null) {
            Log.d(TAG, "User ID: " + user.getUid());
            Log.d(TAG, "Email: " + user.getEmail());
        } else {
            Log.d(TAG, "No user is currently signed in.");
        }


        if (user != null) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Get the user's data
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String phone = document.getString("phone");
                                String apartmentNumber = document.getString("apartmentNumber");
                                String buildingNumber = document.getString("buildingNumber");

                                // Log the user's data
                                Log.d(TAG, "User's name: " + name);
                                Log.d(TAG, "User's email: " + email);
                                Log.d(TAG, "User's phone: " + phone);
                                Log.d(TAG, "User's apartment number: " + apartmentNumber);
                                Log.d(TAG, "User's building number: " + buildingNumber);

                                // Get the TextViews to display the user's data
                                TextView userNameTextView = findViewById(R.id.userName);
                                // Add more TextViews as needed

                                // Set the user's data to the TextViews
                                userNameTextView.setText(name);
                                // Add more setText calls as needed
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                // Action when "Home" is selected
                return true;
            } else if (id == R.id.menu_contact) {
                // Action when "Contact" is selected
                return true;
            } else if (id == R.id.menu_signout) {
                // Action when "Sign Out" is selected
                FirebaseAuth.getInstance().signOut(); // Sign out the user

                // Create an Intent to start the Login activity
                Intent intent = new Intent(HomePage.this, Login.class);
                startActivity(intent);
                finish(); // Close the current activity

                return true;
            } else {
                return false;
            }
        });

        cardMap = findViewById(R.id.mapCard);

        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, GoogleMaps.class));
                Log.w(TAG, "Redirecting to Google Maps page");
            }
        });



    }
}