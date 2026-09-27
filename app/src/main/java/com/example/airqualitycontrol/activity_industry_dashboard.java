package com.example.airqualitycontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_industry_dashboard extends AppCompatActivity {

    TextView mq135Text, mq4Text, mq7Text, dht11TempText, dht11HumText, sensor1Text, sensor2Text;
    DatabaseReference rootRef;
    String industryId;
    String industryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_dashboard);

        // ✅ Bind UI
        mq135Text     = findViewById(R.id.mq135Text);
        mq4Text       = findViewById(R.id.mq4Text);
        mq7Text       = findViewById(R.id.mq7Text);
        dht11TempText = findViewById(R.id.dht11TempText);
        dht11HumText  = findViewById(R.id.dht11HumText);
        sensor1Text   = findViewById(R.id.sensor1Text);
        sensor2Text   = findViewById(R.id.sensor2Text);

        // ✅ Receive UID + IndustryName from login
        industryId   = getIntent().getStringExtra("industryId");   // UID
        industryName = getIntent().getStringExtra("industryName"); // Industry1 / Industry2

        if (industryId == null || industryId.isEmpty()) {
            Toast.makeText(this, "Invalid Industry ID", Toast.LENGTH_SHORT).show();
            goToLogin();
            return;
        }
        if (industryName == null || industryName.isEmpty()) {
            industryName = "Industry1"; // default fallback
        }

        rootRef = FirebaseDatabase.getInstance().getReference();

        // ✅ Attach live listeners
        loadMQ135();
        loadMQ4();
        loadMQ7();
        loadDHT11();
    }

    private void loadMQ135() {
        rootRef.child("Industries").child(industryId).child(industryName)
                .child("MQ135").child("Sensor1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double valObj = snapshot.child("Value").getValue(Double.class);
                        String tsStr  = snapshot.child("Timestamp").getValue(String.class);

                        mq135Text.setText(valObj != null ? String.valueOf(valObj) : "--");
                        if (tsStr != null) {
                            sensor1Text.setText("MQ135 Updated: " + tsStr);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mq135Text.setText("Error");
                    }
                });
    }

    private void loadDHT11() {
        rootRef.child("Industries").child(industryId).child(industryName)
                .child("DHT11").child("Sensor1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double tempObj = snapshot.child("Temperature").getValue(Double.class);
                        Double humObj  = snapshot.child("Humidity").getValue(Double.class);
                        String tsStr   = snapshot.child("Timestamp").getValue(String.class);

                        dht11TempText.setText(tempObj != null ? tempObj + " °C" : "-- °C");
                        dht11HumText.setText(humObj != null ? humObj + " %" : "-- %");
                        if (tsStr != null) {
                            sensor2Text.setText("Updated: " + tsStr);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dht11TempText.setText("Error");
                        dht11HumText.setText("Error");
                    }
                });
    }

    private void loadMQ4() {
        rootRef.child("Industries").child(industryId).child(industryName)
                .child("MQ4").child("Sensor1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double valObj = snapshot.child("Value").getValue(Double.class);
                        mq4Text.setText(valObj != null ? String.valueOf(valObj) : "--");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mq4Text.setText("Error");
                    }
                });
    }

    private void loadMQ7() {
        rootRef.child("Industries").child(industryId).child(industryName)
                .child("MQ7").child("Sensor1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double valObj = snapshot.child("Value").getValue(Double.class);
                        mq7Text.setText(valObj != null ? String.valueOf(valObj) : "--");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mq7Text.setText("Error");
                    }
                });
    }

    private void goToLogin() {
        Intent i = new Intent(this, activity_industry_login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
