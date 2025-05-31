package com.example.leilotparkingstart;

public class ParkingSession {
    private int id;
    private int parkingSpotId;
    private long startTime;
    private long endTime;
    private double cost;
    private String userId;

    public ParkingSession(int parkingSpotId, long startTime, long endTime, double cost, String userId) {
        this.parkingSpotId = parkingSpotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
