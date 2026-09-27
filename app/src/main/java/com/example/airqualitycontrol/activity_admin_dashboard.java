package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.*;

public class activity_admin_dashboard extends AppCompatActivity {

    LinearLayout industryContainer, userContainer;

    DatabaseReference industryRef, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        industryContainer = findViewById(R.id.industryContainer);
        userContainer = findViewById(R.id.userContainer);

        industryRef = FirebaseDatabase.getInstance().getReference("Industries");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        loadIndustries();
        loadUsers();
    }

    // 🔵 LOAD INDUSTRIES
    private void loadIndustries() {

        industryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                industryContainer.removeAllViews();

                for(DataSnapshot data : snapshot.getChildren()) {

                    String name = "" + data.child("name").getValue();
                    String email = "" + data.child("email").getValue();
                    String type = "" + data.child("type").getValue();
                    String location = "" + data.child("location").getValue();

                    TextView tv = new TextView(activity_admin_dashboard.this);
                    tv.setText("Name: " + name +
                            "\nEmail: " + email +
                            "\nType: " + type +
                            "\nLocation: " + location +
                            "\n----------------------");

                    tv.setPadding(10,10,10,10);

                    industryContainer.addView(tv);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    // 🟢 LOAD USERS
    private void loadUsers() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                userContainer.removeAllViews();

                for(DataSnapshot data : snapshot.getChildren()) {

                    String name = "" + data.child("name").getValue();
                    String email = "" + data.child("email").getValue();
                    String phone = "" + data.child("phone").getValue();
                    String location = "" + data.child("location").getValue();

                    TextView tv = new TextView(activity_admin_dashboard.this);
                    tv.setText("Name: " + name +
                            "\nEmail: " + email +
                            "\nPhone: " + phone +
                            "\nLocation: " + location +
                            "\n----------------------");

                    tv.setPadding(10,10,10,10);

                    userContainer.addView(tv);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}