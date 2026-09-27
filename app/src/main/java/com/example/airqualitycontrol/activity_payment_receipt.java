package com.example.airqualitycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class activity_payment_receipt extends AppCompatActivity {

    TextView receiptId, receiptAmount, receiptMethod, receiptDate;
    Button downloadBtn, backDashBtn;
    LinearLayout receiptCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);

        receiptId     = findViewById(R.id.receiptId);
        receiptAmount = findViewById(R.id.receiptAmount);
        receiptMethod = findViewById(R.id.receiptMethod);
        receiptDate   = findViewById(R.id.receiptDate);
        downloadBtn   = findViewById(R.id.downloadBtn);
        backDashBtn   = findViewById(R.id.backDashBtn);
        receiptCard   = findViewById(R.id.receiptCard);

        // Get data from payment screen
        int    amount    = getIntent().getIntExtra("amount", 0);
        String method    = getIntent().getStringExtra("method");
        String paymentId = getIntent().getStringExtra("paymentId");

        String date = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date());

        receiptId.setText(paymentId != null ?
                paymentId.substring(0, 8).toUpperCase() : "N/A");
        receiptAmount.setText("₹" + amount);
        receiptMethod.setText(method);
        receiptDate.setText(date);

        // Download as PDF
        downloadBtn.setOnClickListener(v -> downloadPDF());

        // Back to dashboard
        backDashBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,
                    activity_industry_dashboard.class));
            finish();
        });
    }

    private void downloadPDF() {
        try {
            // Screenshot of receipt card
            receiptCard.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(
                    receiptCard.getDrawingCache());
            receiptCard.setDrawingCacheEnabled(false);

            PdfDocument pdf = new PdfDocument();
            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(
                            bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = pdf.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            canvas.drawBitmap(bitmap, 0, 0, null);
            pdf.finishPage(page);

            // Save to Downloads
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dir, "AirGuard_Receipt_" +
                    System.currentTimeMillis() + ".pdf");
            FileOutputStream fos = new FileOutputStream(file);
            pdf.writeTo(fos);
            pdf.close();
            fos.close();

            Toast.makeText(this,
                    "Receipt saved to Downloads! ✅",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}