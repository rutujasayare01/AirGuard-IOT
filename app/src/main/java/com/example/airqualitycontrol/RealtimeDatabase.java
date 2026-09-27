package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

public class RealtimeDatabase extends AppCompatActivity {

    TextView sensorText, timeText;
    DatabaseReference industryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtimedatabase);

        sensorText = findViewById(R.id.sensor1Text);
        timeText = findViewById(R.id.timeText);

        String location = getIntent().getStringExtra("location");

        if (location == null) {
            Toast.makeText(this, "No location!", Toast.LENGTH_SHORT).show();
            return;
        }

        timeText.setText("Location: " + location);

        // 🔥 SELECT INDUSTRY
        if (location.equalsIgnoreCase("Sadar")) {
            industryRef = FirebaseDatabase.getInstance()
                    .getReference("Industries")
                    .child("Industry1");
        } else {
            industryRef = FirebaseDatabase.getInstance()
                    .getReference("Industries")
                    .child("Industry2");
        }

        // 🔥 GET DATA
        industryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    sensorText.setText("No Data Found");
                    return;
                }

                // ✅ DHT11 (simple object)
                DataSnapshot dhtSnap = snapshot.child("DHT11").child("Sensor1");

                Double temp = dhtSnap.child("Temperature").getValue(Double.class);
                Integer humidity = dhtSnap.child("Humidity").getValue(Integer.class);

                // ✅ MQ135 (last value)
                Integer mq135 = getLastSensorValue(snapshot.child("MQ135"));

                // ✅ MQ4
                Integer mq4 = getLastSensorValue(snapshot.child("MQ4"));

                // ✅ MQ7
                Integer mq7 = getLastSensorValue(snapshot.child("MQ7"));

                // 🔥 DISPLAY CLEAN DATA
                String result =
                        "DHT11\n" +
                                "Temp : " + (temp != null ? temp : "N/A") + " °C\n" +
                                "Humidity : " + (humidity != null ? humidity : "N/A") + " %\n\n" +

                                "MQ135 : " + (mq135 != null ? mq135 : "N/A") + " ppm\n\n" +
                                "MQ4   : " + (mq4 != null ? mq4 : "N/A") + " ppm\n\n" +
                                "MQ7   : " + (mq7 != null ? mq7 : "N/A") + " ppm";

                sensorText.setText(result);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RealtimeDatabase.this,
                        "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔥 FUNCTION TO GET LAST VALUE FROM SENSOR LIST
    private Integer getLastSensorValue(DataSnapshot sensorSnapshot) {

        Integer lastValue = null;

        for (DataSnapshot data : sensorSnapshot.getChildren()) {
            lastValue = data.child("Value").getValue(Integer.class);
        }

        return lastValue;
    }
}