package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.*;

public class activity_payment extends AppCompatActivity {

    RadioGroup paymentGroup;
    RadioButton rbNetBanking, rbUpi, rbCard;
    Button payNowBtn;
    TextView amountText;
    String industryId;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentGroup  = findViewById(R.id.paymentGroup);
        rbNetBanking  = findViewById(R.id.rbNetBanking);
        rbUpi         = findViewById(R.id.rbUpi);
        rbCard        = findViewById(R.id.rbCard);
        payNowBtn     = findViewById(R.id.payNowBtn);
        amountText    = findViewById(R.id.amountText);

        // Get data from dashboard
        industryId = getIntent().getStringExtra("industryId");
        amount     = getIntent().getIntExtra("amount", 500);
        amountText.setText("₹" + amount);

        payNowBtn.setOnClickListener(v -> {
            int selectedId = paymentGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String method = "";
            if (selectedId == R.id.rbNetBanking) method = "Net Banking";
            else if (selectedId == R.id.rbUpi)   method = "UPI";
            else if (selectedId == R.id.rbCard)   method = "Debit/Credit Card";

            processPayment(method);
        });
    }

    private void processPayment(String method) {
        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference();

        String paymentId = db.child("Payments")
                .child(industryId).push().getKey();

        // Save payment record
        db.child("Payments").child(industryId).child(paymentId)
                .child("amount").setValue(amount);
        db.child("Payments").child(industryId).child(paymentId)
                .child("method").setValue(method);
        db.child("Payments").child(industryId).child(paymentId)
                .child("status").setValue("paid");
        db.child("Payments").child(industryId).child(paymentId)
                .child("time").setValue(System.currentTimeMillis());

        // Update penalty status → paid
        // (admin dashboard will auto-update via listener)
        db.child("Penalties").child(industryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot pen : snapshot.getChildren()) {
                            if ("pending".equals(
                                    pen.child("status").getValue(String.class))) {
                                pen.getRef().child("status").setValue("paid");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // Go to receipt screen
        Intent i = new Intent(this, activity_payment.class);
        i.putExtra("amount",    2000);
        i.putExtra("method",    method);
        i.putExtra("paymentId", paymentId);
        i.putExtra("industryId", industryId);
        startActivity(i);
        finish();
    }
}