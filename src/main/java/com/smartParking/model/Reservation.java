package com.smartParking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int userId;
    private int spotId;
    private int lotId;
    private String lotName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private BigDecimal penalty;
    private BigDecimal cost;



    // Constructors
    public Reservation(int reservationId, int userId, int spotId, int lotId, String lotName, LocalDateTime startTime, LocalDateTime endTime, String status, BigDecimal penalty, BigDecimal cost) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.spotId = spotId;
        this.lotId = lotId;
        this.lotName = lotName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.penalty = penalty;
        this.cost = cost;
    }

    public Reservation(int userId, int spotId, int lotId, String lotName,LocalDateTime startTime, LocalDateTime endTime, String status, BigDecimal penalty, BigDecimal cost) {
        this.userId = userId;
        this.spotId = spotId;
        this.lotId = lotId;
        this.lotName = lotName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.penalty = penalty;
        this.cost = cost;
    }

    public Reservation() {
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
