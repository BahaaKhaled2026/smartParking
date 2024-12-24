package com.smartParking.model;

import java.time.LocalDateTime;

public class ParkingLot {
    private int lotId;
    private String name;
    private String location;
    private int capacity;
    private LocalDateTime createdAt;

    // Constructors
    public ParkingLot(int lotId, String name, String location, int capacity, LocalDateTime createdAt) {
        this.lotId = lotId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.createdAt = createdAt;
    }

    public ParkingLot(String name, String location, int capacity) {
        this.name = name;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
