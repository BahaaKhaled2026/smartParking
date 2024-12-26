package com.smartParking.service;

import com.smartParking.model.Reservation;

import java.util.List;

public interface ReservationService {
    int createReservation(Reservation reservation);
    boolean cancelReservation(int reservationId);
    List<Reservation> getUserReservations(int userId);
    void applyOverStayPenalties();
    void applyNoShowPenalties();
    void releaseExpiredReservations();
    void finishReservation(int reservationId);
}
