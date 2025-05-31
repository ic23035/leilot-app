package com.example.leilotparkingstart;

public class UserStats {
    private int totalSessions;
    private long totalTime;
    private double totalCost;

    public UserStats(int totalSessions, long totalTime, double totalCost) {
        this.totalSessions = totalSessions;
        this.totalTime = totalTime;
        this.totalCost = totalCost;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public double getTotalCost() {
        return totalCost;
    }
}