package com.example.syndisync;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button signInButton;
    EditText email, password;
    String emailContent, passwordContent;
    TextView signUpRedirectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.signIn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUpRedirectionText = findViewById(R.id.redToSignUp);

        // redirect to sign up page from login page
        signUpRedirectionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                Log.w(TAG, "Redirecting to sign up page");
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailContent = email.getText().toString();
                passwordContent = password.getText().toString();
                Log.w(TAG, emailContent);

                mAuth.signInWithEmailAndPassword(emailContent, passwordContent)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_SHORT).show();

                                    // redirect to home page after 3 seconds
                                    new Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    startActivity(new Intent(Login.this, HomePage.class));
                                                }
                                            },
                                            3000);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

    }
}
