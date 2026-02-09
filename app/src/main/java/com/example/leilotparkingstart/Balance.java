package com.example.leilotparkingstart;

import android.content.Context;
import android.content.SharedPreferences;

public class Balance {
    private static Balance instance;
    private final Context context;
    private float balance = 0;

    private Balance(Context context) {
        this.context = context.getApplicationContext();
    }


    public static void init(Context context) {
        if (instance == null) {
            instance = new Balance(context);
        }
    }

    public static Balance getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call Balance.init(context) before using getInstance()");
        }
        return instance;
    }

    public float getBalance() {
        return balance;
    }

    public void updateBalance(float charge) {
        this.balance -= charge;
        updateUserBalance(balance);
    }

    public void loadUserBalance() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        balance = sharedPreferences.getFloat("balance", 0);
    }

    public void updateUserBalance(float newBalance) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("balance", newBalance);
        editor.apply();
    }


    public float addBalance(float selectedAmount) {
        balance += selectedAmount;
        updateUserBalance(balance);
        return balance;
    }
}