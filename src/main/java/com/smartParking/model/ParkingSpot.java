package com.smartParking.model;

public class ParkingSpot {
    private int spotId;
    private int lotId;
    private String spotNumber;
    private String type; // REGULAR, DISABLED, EV
    private String status; // AVAILABLE, OCCUPIED, RESERVED

    // Constructors
    public ParkingSpot(int spotId, int lotId, String spotNumber, String type, String status) {
        this.spotId = spotId;
        this.lotId = lotId;
        this.spotNumber = spotNumber;
        this.type = type;
        this.status = status;
    }

    public ParkingSpot(int lotId, String spotNumber, String type, String status) {
        this.lotId = lotId;
        this.spotNumber = spotNumber;
        this.type = type;
        this.status = status;
    }

    public ParkingSpot() {
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
