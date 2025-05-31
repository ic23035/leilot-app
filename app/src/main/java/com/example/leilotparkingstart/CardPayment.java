package com.example.leilotparkingstart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CardPayment extends AppCompatActivity {
    private float selectedAmount = 0;

    // method that retrieves selected amount & call it on main
    public void retrieveSelectedAmount(){
        // Retrieve the selected amount passed from MainActivity
        Intent intent = getIntent();
        selectedAmount = intent.getFloatExtra("selectedAmount",0);
        payButton();
        /*// Display selected amount
        TextView selectedAmountText = findViewById(R.id.selectedAmountText);
        selectedAmountText.setText("Amount to Pay: $" + selectedAmount);*/
    }

    // method that sends the amount back to main and then main -> balance.addamount method and it gets displayed
    public void addAmountToBalance(){
        // go back to main
        Intent returnFinalAmount = new Intent();
        returnFinalAmount.putExtra("addedAmount", selectedAmount);
        setResult(RESULT_OK, returnFinalAmount);
        finish();
    }

    public void payButton(){
        //create btn instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(view -> {addAmountToBalance();});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        retrieveSelectedAmount();
    }
}
