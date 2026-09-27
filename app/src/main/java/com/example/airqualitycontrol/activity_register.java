package com.example.airqualitycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;

public class activity_register extends AppCompatActivity {

    private EditText name, email, password, phone;
    private Spinner locationSpinner;
    private Button registerBtn;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        locationSpinner = findViewById(R.id.locationSpinner);
        registerBtn = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.locations_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        registerBtn.setOnClickListener(v -> {

            String userName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String userPhone = phone.getText().toString().trim();
            String location = locationSpinner.getSelectedItem().toString();

            if (userName.isEmpty() || userEmail.isEmpty() ||
                    userPassword.isEmpty() || userPhone.isEmpty()) {

                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 CREATE USER IN AUTH
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();

                            // 🔥 SAVE DATA USING UID (IMPORTANT)
                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", userName);
                            userMap.put("email", userEmail);
                            userMap.put("phone", userPhone);
                            userMap.put("location", location);

                            databaseReference.setValue(userMap)
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(activity_register.this,
                                                activity_dashboard.class);

                                        intent.putExtra("location", location);
                                        startActivity(intent);
                                        finish();
                                    });

                        } else {
                            Toast.makeText(this,
                                    "Register Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}