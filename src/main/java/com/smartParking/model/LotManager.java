package com.smartParking.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class LotManager {
    private int lotId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private int capacity;
    private BigDecimal totalRevenue;
    private BigDecimal totalPenalty;
    private int occupied ;
    private double occupancy_rate ;

    public LotManager(int lotId, BigDecimal longitude, BigDecimal latitude,
                      BigDecimal totalRevenue, BigDecimal totalPenalty, int capacity, int occupied) {
        this.lotId = lotId;
        this.longitude = longitude.setScale(3, RoundingMode.DOWN);
        this.latitude = latitude.setScale(3, RoundingMode.DOWN);
        this.capacity = capacity;
        this.totalRevenue = totalRevenue;
        this.totalPenalty = totalPenalty;
        this.occupied = occupied;
        this.occupancy_rate = Math.round(((double) occupied / capacity)*100.0)/100.0 ;

    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }


    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalPenalty() {
        return totalPenalty;
    }

    public void setTotalPenalty(BigDecimal totalPenalty) {
        this.totalPenalty = totalPenalty;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public double getOccupancy_rate() {
        return occupancy_rate;
    }

    public void setOccupancy_rate(double occupancy_rate) {
        this.occupancy_rate = occupancy_rate;
    }
}
