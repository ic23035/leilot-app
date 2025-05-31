package com.example.leilotparkingstart;

import java.io.Serializable;

public class ParkingSpot implements Serializable {
    private int id;
    private String name;
    private double lat;
    private double lng;
    private boolean available;
    private double pricePerHour;
    private String notes;

    public ParkingSpot(int id, String name, double lat, double lng, boolean available, double pricePerHour, String notes) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.available = available;
        this.pricePerHour = pricePerHour;
        this.notes = notes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public boolean isAvailable() { return available; }
    public double getPricePerHour() { return pricePerHour; }
    public String getNotes() { return notes; }

}
