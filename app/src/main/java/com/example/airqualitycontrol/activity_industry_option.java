package com.example.airqualitycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class activity_industry_option extends AppCompatActivity {

    // ⚠️ Button → LinearLayout change karo
    LinearLayout loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_option);

        loginBtn    = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        // Logic same ✅
        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, activity_industry_login.class));
        });

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, activity_industry_register.class));
        });
    }
}