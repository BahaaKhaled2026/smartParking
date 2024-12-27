package com.smartParking;

public class NotificationMessage {

    private int reservationId;
    private String message;

    public NotificationMessage(int reservationId, String message) {
        this.reservationId = reservationId;
        this.message = message;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}