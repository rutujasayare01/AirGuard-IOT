package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

public class activity_prediction extends AppCompatActivity {

    TextView aqiText, statusText, precautionText;
    DatabaseReference industryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        aqiText = findViewById(R.id.aqiText);
        statusText = findViewById(R.id.statusText);
        precautionText = findViewById(R.id.precautionText);

        String location = getIntent().getStringExtra("location");

        if (location == null) {
            Toast.makeText(this, "No location!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 SELECT INDUSTRY (SAME AS REALTIME)
        if (location.equalsIgnoreCase("Sadar")) {
            industryRef = FirebaseDatabase.getInstance()
                    .getReference("Industries")
                    .child("Industry1");
        } else {
            industryRef = FirebaseDatabase.getInstance()
                    .getReference("Industries")
                    .child("Industry2");
        }

        // 🔥 FETCH DATA
        industryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    aqiText.setText("No Data");
                    return;
                }

                // ✅ GET LAST VALUES (SAME LOGIC AS REALTIME)
                Integer mq135 = getLastSensorValue(snapshot.child("MQ135"));
                Integer mq4   = getLastSensorValue(snapshot.child("MQ4"));
                Integer mq7   = getLastSensorValue(snapshot.child("MQ7"));

                if (mq135 == null || mq4 == null || mq7 == null) {
                    aqiText.setText("Incomplete Data");
                    return;
                }

                // 🔥 AQI CALCULATION
                int aqi = (mq135 + mq4 + mq7) / 3;

                aqiText.setText("AQI: " + aqi);

                // 🔥 STATUS + PRECAUTIONS
                if (aqi <= 200) {
                    statusText.setText("Good 😊");
                    precautionText.setText("Air quality is good. Enjoy outdoor activities.");
                }
                else if (aqi <= 400) {
                    statusText.setText("Moderate 😐");
                    precautionText.setText("Sensitive people should reduce outdoor activity.");
                }
                else if (aqi <= 600) {
                    statusText.setText("Poor 😷");
                    precautionText.setText("Wear mask. Avoid long outdoor exposure.");
                }
                else if (aqi <= 900) {
                    statusText.setText("Very Poor 🚫");
                    precautionText.setText("Stay indoors. Use air purifier.");
                }
                else {
                    statusText.setText("Hazardous ☠️");
                    precautionText.setText("Avoid going outside. Serious health risk.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(activity_prediction.this,
                        error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔥 SAME FUNCTION AS REALTIME
    private Integer getLastSensorValue(DataSnapshot sensorSnapshot) {

        Integer lastValue = null;

        for (DataSnapshot data : sensorSnapshot.getChildren()) {
            lastValue = data.child("Value").getValue(Integer.class);
        }

        return lastValue;
    }
}