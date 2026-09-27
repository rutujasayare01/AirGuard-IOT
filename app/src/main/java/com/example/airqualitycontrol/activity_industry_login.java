package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_industry_login extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_login);

        // ✅ Match IDs with your XML
        emailInput    = findViewById(R.id.loginEmail);
        passwordInput = findViewById(R.id.loginPassword);
        loginBtn      = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(activity_industry_login.this,
                            "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // ✅ Direct UID use
                                    String uid = user.getUid();

                                    // ✅ Pass UID + IndustryName to dashboard
                                    Intent i = new Intent(activity_industry_login.this,
                                            activity_industry_dashboard.class);
                                    i.putExtra("industryId", uid);
                                    i.putExtra("industryName", "Industry1");
                                    // Agar Industry2 ka account hai to "Industry2" pass karo
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                Toast.makeText(activity_industry_login.this,
                                        "Login failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
