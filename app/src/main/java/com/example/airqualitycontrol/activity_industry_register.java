package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_industry_register extends AppCompatActivity {

    EditText name, email, password, phone, type;
    Spinner locationSpinner;
    Button registerBtn;
    FirebaseAuth mAuth;
    DatabaseReference industryRef;
    String[] locations = {"Nagpur", "Sadar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_register);

        try {
            name            = findViewById(R.id.industryName);
            email           = findViewById(R.id.industryEmail);
            password        = findViewById(R.id.industryPassword);
            phone           = findViewById(R.id.industryPhone);
            type            = findViewById(R.id.industryType);
            locationSpinner = findViewById(R.id.locationSpinner);
            registerBtn     = findViewById(R.id.registerBtn);

            mAuth       = FirebaseAuth.getInstance();
            industryRef = FirebaseDatabase.getInstance()
                    .getReference("Industries");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    locations
            );
            locationSpinner.setAdapter(adapter);

            registerBtn.setOnClickListener(v ->
                    registerIndustry());

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void registerIndustry() {
        try {
            String n   = name.getText().toString().trim();
            String e   = email.getText().toString().trim();
            String p   = password.getText().toString().trim();
            String ph  = phone.getText().toString().trim();
            String t   = type.getText().toString().trim();
            String loc = locationSpinner
                    .getSelectedItem().toString();

            if (n.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this,
                        "Fill all required fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (p.length() < 6) {
                Toast.makeText(this,
                        "Password min 6 characters",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            registerBtn.setEnabled(false);
            registerBtn.setText("Registering...");

            mAuth.createUserWithEmailAndPassword(e, p)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user =
                                    mAuth.getCurrentUser();
                            String uid = user.getUid();

                            industryRef.child(uid)
                                    .child("name").setValue(n);
                            industryRef.child(uid)
                                    .child("email").setValue(e);
                            industryRef.child(uid)
                                    .child("phone").setValue(ph);
                            industryRef.child(uid)
                                    .child("type").setValue(t);
                            industryRef.child(uid)
                                    .child("location").setValue(loc);
                            industryRef.child(uid)
                                    .child("role").setValue("industry");

                            Toast.makeText(this,
                                    "Industry Registered ✅",
                                    Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(this,
                                    activity_industry_dashboard.class);
                            i.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();

                        } else {
                            registerBtn.setEnabled(true);
                            registerBtn.setText("Register");

                            String error = task.getException()
                                    .getMessage();

                            if (error.contains("email")) {
                                email.setError(
                                        "Email already registered!");
                                email.requestFocus();
                            } else {
                                Toast.makeText(this,
                                        "Error: " + error,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(this,
                    "Crash: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}