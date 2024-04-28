package com.example.syndisync;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    TextView signInRedirectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signInRedirectionText = findViewById(R.id.backToSignIn);

        // redirect to sign in page from register page

        signInRedirectionText.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
        });
    }
}
