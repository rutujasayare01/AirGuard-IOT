package com.example.airqualitycontrol;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_profile extends AppCompatActivity {

    TextView name,email,phone,location;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profilePhone);
        location = findViewById(R.id.profileLocation);

        auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid);

        databaseReference.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                DataSnapshot snapshot = task.getResult();

                name.setText("Name: " + snapshot.child("name").getValue(String.class));
                email.setText("Email: " + snapshot.child("email").getValue(String.class));
                phone.setText("Phone: " + snapshot.child("phone").getValue(String.class));
                location.setText("Location: " + snapshot.child("location").getValue(String.class));

            }

        });

    }
}