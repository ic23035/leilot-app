package com.example.leilotparkingstart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class list_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParkingSpotAdapter adapter;
    private Button startParkingButton;
    private ParkingSpot selectedSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        startParkingButton = findViewById(R.id.start_parking_button);
        startParkingButton.setVisibility(View.GONE); // Κρύβουμε το κουμπί στην αρχή

        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);
        List<ParkingSpot> spots = dbHelper.getAllAvailableSpots();

        // Δημιουργία του adapter με το listener
        adapter = new ParkingSpotAdapter(spots, new ParkingSpotAdapter.OnParkingSpotClickListener() {
            @Override
            public void onParkingSpotClick(ParkingSpot spot) {
                selectedSpot = spot;  // Αποθήκευση της επιλεγμένης θέσης
                startParkingButton.setVisibility(View.VISIBLE);  // Εμφάνιση του κουμπιού
            }
        });
        recyclerView.setAdapter(adapter);

        startParkingButton.setOnClickListener(v -> {

            Log.d("Parking", "Ξεκινά στάθμευση για: " + selectedSpot.getName());
            // Μπορείς εδώ να καλέσεις κάποιο intent ή να αποθηκεύσεις το spot
            Intent intent = new Intent(list_activity.this, FormActivity.class);
            intent.putExtra("selectedSpot", selectedSpot);
            startActivity(intent);

            // Εδώ ο άλλος θα υλοποιήσει τη λογική στάθμευσης
        });
    }
}
