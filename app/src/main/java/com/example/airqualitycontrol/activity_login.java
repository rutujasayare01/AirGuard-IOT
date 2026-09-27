package com.example.airqualitycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class activity_login extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;

    FirebaseAuth auth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {

            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();

                            if (user == null) {
                                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String uid = user.getUid();

                            // ✅ MUST MATCH FIREBASE NODE NAME EXACTLY
                            userRef = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(uid);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    if (snapshot.exists()) {

                                        String location = snapshot.child("location")
                                                .getValue(String.class);

                                        if (location == null) {
                                            location = "Unknown";
                                        }

                                        Toast.makeText(activity_login.this,
                                                "Login Successful",
                                                Toast.LENGTH_SHORT).show();

                                        // ✅ PASS LOCATION TO DASHBOARD
                                        Intent intent = new Intent(activity_login.this,
                                                activity_dashboard.class);

                                        intent.putExtra("location", location);

                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(activity_login.this,
                                                "User data not found in Firebase",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(activity_login.this,
                                            error.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(activity_login.this,
                                    "Login Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}