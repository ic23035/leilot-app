package com.example.leilotparkingstart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FormActivity extends AppCompatActivity {

    private EditText editTextLicensePlate, editTextEmail;
    private Spinner spinnerDuration;
    private Button buttonGoToPayment;
    private Handler timeoutHandler;
    private Runnable timeoutRunnable;
    private static final long RESERVATION_TIMEOUT = 5 * 60 * 1000; // 5 minutes in ms
    ParkingSpot selectedSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        selectedSpot = (ParkingSpot) getIntent().getSerializableExtra("selectedSpot");

        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);
        dbHelper.reserveSpot(selectedSpot.getId());

        startReservationTimeout();

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            dbHelper.releaseSpot(selectedSpot.getId());
            cancelReservationTimeout();
            finish();  // Close this activity
        });

        // Initialize views
        editTextLicensePlate = findViewById(R.id.editTextLicensePlate);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerDuration = findViewById(R.id.spinnerDuration);
        buttonGoToPayment = findViewById(R.id.buttonGoToPayment);

        // Set up Spinner (Dropdown for Duration)
        setupSpinner();

        // Button click listener
        buttonGoToPayment.setOnClickListener(v -> {
            if (validateForm()) {
                // Proceed to payment (replace with your logic)
                cancelReservationTimeout();
                Toast.makeText(
                        this,
                        "Redirecting to payment...",
                        Toast.LENGTH_SHORT
                ).show();
                Intent intent2 = new Intent(FormActivity.this, ParkingActivity.class);
                intent2.putExtra("selectedSpot", selectedSpot);
                startActivity(intent2);
                finish();
            }

        });
    }
    private void startReservationTimeout() {
        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);
        timeoutHandler = new Handler();
        timeoutRunnable = () -> {
            // Release reservation after timeout
            dbHelper.releaseSpot(selectedSpot.getId());
            Toast.makeText(this, "Reservation timed out", Toast.LENGTH_SHORT).show();
            finish(); // close the form
        };
        timeoutHandler.postDelayed(timeoutRunnable, RESERVATION_TIMEOUT);
    }

    private void cancelReservationTimeout() {
        if (timeoutHandler != null && timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
    }


    private void setupSpinner() {
        // Example durations (modify as needed)
        String[] durations = {"½ hour", "1 hour", "1½ hours","2 hours","2½ hours","3 hours", "3½ hours", "All day"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                durations
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);
        // Release reservation
        dbHelper.releaseSpot(selectedSpot.getId());
        cancelReservationTimeout(); // Also stop the timeout so it doesn't trigger after
        super.onBackPressed();
    }

    private boolean validateForm() {
        String licensePlate = editTextLicensePlate.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (licensePlate.isEmpty()) {
            editTextLicensePlate.setError("License plate is required");
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            return false;
        }

        return true;
    }


}