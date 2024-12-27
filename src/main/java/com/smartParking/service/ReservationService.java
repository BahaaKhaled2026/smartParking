package com.smartParking.service;

import com.smartParking.model.Reservation;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationService {
    Number createReservation(Reservation reservation , boolean reserve);
    boolean cancelReservation(int reservationId);
    List<Reservation> getUserReservations(int userId);
    void applyOverStayPenalties(Reservation reservation);
    void applyNoShowPenalties();
    void releaseExpiredReservations();
    void finishReservation(int reservationId);
    void startReservations();
    void arriveAtSpot(int reservationId);
    void tenMinuteLeftNotification();
}

