package com.example.leilotparkingstart;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParkingActivity extends AppCompatActivity {
    private int seconds = 0;
    private boolean running = false;
    private TextView textViewTimer;
    private Handler handler = new Handler();
    ParkingSpot selectedSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_activity);

        TextView costTextView = findViewById(R.id.cost);
        textViewTimer = findViewById(R.id.textViewTimer);
        Button buttonStartStop = findViewById(R.id.startStopBtn);

        selectedSpot = (ParkingSpot) getIntent().getSerializableExtra("selectedSpot");

        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);

        UserStats user = dbHelper.getCachedUserStats("1");
        if (user == null) {
            Toast.makeText(this, "User stats not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSpot == null) {
            Toast.makeText(this, "Spot not found!", Toast.LENGTH_LONG).show();
        } else {
            costTextView.setText("Parking selected: " + selectedSpot.getName()
                    + " | Charge per hour: " + selectedSpot.getPricePerHour() + " $");

            Toast.makeText(this, "Using spot: " + selectedSpot.getName(), Toast.LENGTH_SHORT).show();
        }

        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    running = false;
                    buttonStartStop.setText("Start Parking Session");
                    int hexColor = Color.parseColor("#E86E58");
                    buttonStartStop.setBackgroundTintList(ColorStateList.valueOf(hexColor));

                    calculateTotalCost();
                    resetTimer();

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(ParkingActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }, 1500);





                } else {
                    running = true;
                    buttonStartStop.setText("Stop Parking Session");
                    buttonStartStop.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
            }
        });

        runTimer();
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
                textViewTimer.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void calculateTotalCost() {
        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);
        UserStats user = dbHelper.getCachedUserStats("1");

        int minutesSpent = seconds / 60;

        if (selectedSpot != null) {
            // Υπολογισμός κόστους
            double totalCost = (minutesSpent / 60.0) * selectedSpot.getPricePerHour();

            // Ενημέρωση στατιστικών
            dbHelper.updateUserStats(1, user.getTotalSessions() + 1, user.getTotalTime() + seconds, user.getTotalCost() + totalCost);
            Balance.init(getApplicationContext());
            Balance.getInstance().updateBalance((float) totalCost);

            // Επανεκκίνηση των στατιστικών μετά την ενημέρωση
            user = dbHelper.getCachedUserStats("1");
            System.out.println("Updated Total Time: " + user.getTotalTime());

            // Εμφάνιση κόστους
            TextView costTextView = findViewById(R.id.cost);
            costTextView.setText("Total Cost: " + String.format("%.2f", totalCost) + " $");
            Toast.makeText(this, "Price has been updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No parking spot selected!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void resetTimer() {
        seconds = 0;
        textViewTimer.setText("00:00:00");
    }
}
