package com.smartParking.model;

import java.time.LocalDateTime;

public class ParkingLot {
    private int lotId;
    private String name;
    private String longitude;
    private String latitude;
    private int capacity;
    private LocalDateTime createdAt;

    public ParkingLot() {
    }

    public ParkingLot(int lotId, String name, String longitude, String latitude, int capacity, LocalDateTime createdAt) {
        this.lotId = lotId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.capacity = capacity;
        this.createdAt = createdAt;
    }

    public ParkingLot(String name, String longitude, String latitude, int capacity) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.capacity = capacity;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
