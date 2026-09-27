package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_admin_login extends AppCompatActivity {

    EditText adminUsername, adminPassword;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminUsername = findViewById(R.id.adminUsername);
        adminPassword = findViewById(R.id.adminPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = adminUsername.getText().toString();
                String password = adminPassword.getText().toString();

                if(username.equals("admin") && password.equals("admin123")) {

                    Toast.makeText(activity_admin_login.this,"Login Successful",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(activity_admin_login.this, activity_dashboard.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(activity_admin_login.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}