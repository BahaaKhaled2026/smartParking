package com.smartParking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int userId;
    private int spotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // ACTIVE, COMPLETED, CANCELLED
    private BigDecimal penalty;

    // Constructors
    public Reservation(int reservationId, int userId, int spotId, LocalDateTime startTime, LocalDateTime endTime, String status, BigDecimal penalty) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.spotId = spotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.penalty = penalty;
    }

    public Reservation(int userId, int spotId, LocalDateTime startTime, LocalDateTime endTime, String status, BigDecimal penalty) {
        this.userId = userId;
        this.spotId = spotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.penalty = penalty;
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
}
