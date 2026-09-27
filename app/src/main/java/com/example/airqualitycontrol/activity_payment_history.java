package com.example.airqualitycontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class activity_payment_history extends AppCompatActivity {

    RecyclerView historyRecycler;
    List<Map<String, Object>> paymentList = new ArrayList<>();
    String industryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        historyRecycler = findViewById(R.id.historyRecycler);
        historyRecycler.setLayoutManager(
                new LinearLayoutManager(this));

        industryId = getIntent().getStringExtra("industryId");

        loadHistory();
    }

    private void loadHistory() {
        FirebaseDatabase.getInstance()
                .getReference("Payments")
                .child(industryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        paymentList.clear();
                        for (DataSnapshot d : snap.getChildren()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("amount",
                                    d.child("amount").getValue());
                            map.put("method",
                                    d.child("method").getValue());
                            map.put("status",
                                    d.child("status").getValue());
                            map.put("time",
                                    d.child("time").getValue());
                            paymentList.add(map);
                        }
                        Collections.reverse(paymentList);
                        historyRecycler.setAdapter(
                                new HistoryAdapter(paymentList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {}
                });
    }

    // Adapter inside same file
    class HistoryAdapter extends
            RecyclerView.Adapter<HistoryAdapter.VH> {

        List<Map<String, Object>> list;

        HistoryAdapter(List<Map<String, Object>> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent,
                                     int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item_payment_history,
                            parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            Map<String, Object> item = list.get(pos);
            h.amount.setText("₹" + item.get("amount"));
            h.method.setText(String.valueOf(item.get("method")));
            h.status.setText(String.valueOf(item.get("status")));

            long time = Long.parseLong(
                    String.valueOf(item.get("time")));
            h.date.setText(new SimpleDateFormat(
                    "dd MMM yyyy", Locale.getDefault())
                    .format(new Date(time)));

            if ("paid".equals(item.get("status"))) {
                h.status.setTextColor(
                        getResources().getColor(android.R.color.holo_green_dark));
            }
        }

        @Override
        public int getItemCount() { return list.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView amount, method, status, date;
            VH(View v) {
                super(v);
                amount = v.findViewById(R.id.histAmount);
                method = v.findViewById(R.id.histMethod);
                status = v.findViewById(R.id.histStatus);
                date   = v.findViewById(R.id.histDate);
            }
        }
    }
}