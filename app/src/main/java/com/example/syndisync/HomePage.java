package com.example.syndisync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                // Action when "Home" is selected
                return true;
            } else // Action when "Sign Out" is selected
                if (id == R.id.menu_contact) {
                // Action when "Contact" is selected
                return true;
            } else return id == R.id.menu_signout;
        });
    }
}