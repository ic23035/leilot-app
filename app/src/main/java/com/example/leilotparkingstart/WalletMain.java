package com.example.leilotparkingstart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WalletMain extends AppCompatActivity {

    // Αντικείμενο Balance
    com.example.leilotparkingstart.Balance balanceAction;

    // Τρέχον υπόλοιπο
    protected float balance = 0;

    // Ποσό που επιλέγει ο χρήστης
    private float selectedAmount = 0;
    private Button selectedButton = null;

    private void selectAmount(float amount, Button selectedButton) {
        selectedAmount = amount;
        highlightSelectedButton(selectedButton);
    }

    private void highlightSelectedButton(Button button) {
        resetSelectedButton();
        selectedButton = button;
        selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
    }

    private void resetSelectedButton() {
        if (selectedButton != null) {
            selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
            selectedButton = null;
        }
    }

    private void buttons() {
        Button button10usd = findViewById(R.id.button10usd);
        Button button20usd = findViewById(R.id.button20usd);
        Button button50usd = findViewById(R.id.button50usd);
        proceedtoPaymentButton();

        button10usd.setOnClickListener(view -> selectAmount(10, button10usd));
        button20usd.setOnClickListener(view -> selectAmount(20, button20usd));
        button50usd.setOnClickListener(view -> selectAmount(50, button50usd));
    }

    public void proceedtoPaymentButton() {
        Button proceedToPaymentButton = findViewById(R.id.cardButton);
        proceedToPaymentButton.setOnClickListener(view -> {
            Intent intent = new Intent(WalletMain.this, com.example.leilotparkingstart.CardPayment.class);
            intent.putExtra("selectedAmount", selectedAmount);
            startActivityForResult(intent, 1);
        });
    }

    private void displayUserBalance() {
        float currentBalance = Balance.getInstance().getBalance();
        TextView balanceText = findViewById(R.id.balanceTextView);
        balanceText.setText("Available Amount: $" + currentBalance);
    }

    private void addAmountToBalance(Intent data) {
        float amountAdded = data.getFloatExtra("addedAmount", 0);
        balance = balanceAction.addBalance(amountAdded);
        displayUserBalance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            addAmountToBalance(data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_main);

        // ✅ Αρχικοποίηση του singleton Balance
        Balance.init(getApplicationContext());

        balanceAction = Balance.getInstance();
        balanceAction.loadUserBalance();

        buttons();
        displayUserBalance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        float currentBalance = Balance.getInstance().getBalance();
        TextView balanceText = findViewById(R.id.balanceTextView);
        balanceText.setText("Available Amount: $" + currentBalance);
    }
}
