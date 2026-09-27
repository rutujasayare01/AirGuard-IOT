package com.example.airqualitycontrol;

  // ⚠️ Change this to YOUR package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout userBtn, adminBtn, industryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        userBtn     = findViewById(R.id.userBtn);
        adminBtn    = findViewById(R.id.adminBtn);
        industryBtn = findViewById(R.id.industryBtn);

        // Admin click → go to Admin Login
        adminBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, activity_admin_login.class));
        });


        industryBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, activity_industry_option.class));
        });

        // User click → go to User Login
        userBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, activity_option.class));
        });
    }
}