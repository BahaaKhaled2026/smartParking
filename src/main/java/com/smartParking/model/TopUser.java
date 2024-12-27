package com.smartParking.model;
import java.time.LocalDateTime;

public class TopUser {
    private int reservationCount;
    private String userName;
    private String userEmail;
    private String licensePlate;
    private LocalDateTime accountCreated;

    public TopUser(int reservationCount, String userName, String userEmail, String licensePlate, LocalDateTime accountCreated) {
        this.reservationCount = reservationCount;
        this.userName = userName;
        this.userEmail = userEmail;
        this.licensePlate = licensePlate;
        this.accountCreated = accountCreated;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public LocalDateTime getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(LocalDateTime accountCreated) {
        this.accountCreated = accountCreated;
    }
}
