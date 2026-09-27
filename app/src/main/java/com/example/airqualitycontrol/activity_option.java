package com.example.airqualitycontrol;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class activity_option extends AppCompatActivity {

    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(v -> {

            startActivity(new Intent(this, activity_login.class));

        });

        registerBtn.setOnClickListener(v -> {

            startActivity(new Intent(this, activity_register.class));

        });
    }
}