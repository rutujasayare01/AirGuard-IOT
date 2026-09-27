package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class activity_dashboard extends AppCompatActivity {

    Button myProfileBtn, realtimeBtn,predictionBtn;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myProfileBtn = findViewById(R.id.myProfileBtn);
        realtimeBtn = findViewById(R.id.realtimeBtn);
        predictionBtn = findViewById(R.id.predictionBtn);

        location = getIntent().getStringExtra("location");

        if (location == null) {
            location = "Unknown";
        }

        Toast.makeText(this, "Location: " + location, Toast.LENGTH_SHORT).show();

        // Profile
        myProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, activity_profile.class));
        });

        // Realtime
        realtimeBtn.setOnClickListener(v -> {

            Intent intent = new Intent(this, RealtimeDatabase.class);
            intent.putExtra("location", location); // 🔥 PASS AGAIN
            startActivity(intent);
        });
        predictionBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_prediction.class);
            intent.putExtra("location", location); // 🔥 pass location
            startActivity(intent);
        });

    }
}