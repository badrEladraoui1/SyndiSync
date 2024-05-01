package com.example.syndisync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView signInRedirectionText;
    EditText emailInput, passwordInput;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signInRedirectionText = findViewById(R.id.backToSignIn);
        emailInput = findViewById(R.id.editTextTextEmailAddress);
        passwordInput = findViewById(R.id.editTextTextPassword);
        registerButton = findViewById(R.id.registerBtn);
        EditText nameInput = findViewById(R.id.editTextTextName);
        EditText phoneInput = findViewById(R.id.editTextPhone);
        EditText apartmentNumberInput = findViewById(R.id.editApartmentNumber);
        EditText buildingNumberInput = findViewById(R.id.editBuildingNumber);

        // redirect to sign in page from register page
        signInRedirectionText.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String name = nameInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String apartmentNumber = apartmentNumberInput.getText().toString();
            String buildingNumber = buildingNumberInput.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User account created successfully
                                String userId = mAuth.getCurrentUser().getUid();


                                // Store additional user information in Firestore
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("name", name);
                                user.put("phone", phone);
                                user.put("apartmentNumber", apartmentNumber);
                                user.put("buildingNumber", buildingNumber);


                                db.collection("users")
                                        .document(userId)
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> Log.d("Register", "DocumentSnapshot successfully written!"))
                                        .addOnFailureListener(e -> Log.w("Register", "Error writing document", e));
                                Toast.makeText(Register.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                                // rediect to login page after 3 seconds
                                new android.os.Handler().postDelayed(
                                        () -> startActivity(new Intent(Register.this, Login.class)),
                                        3000);

                            } else {
                                // If sign up fails, display a message to the user.
                                Log.w("Register", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }
}
