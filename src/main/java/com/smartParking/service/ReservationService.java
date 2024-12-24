package com.smartParking.service;

import com.smartParking.model.Reservation;
import java.math.BigDecimal;
import java.util.List;

public interface ReservationService {
    int createReservation(Reservation reservation);
    boolean cancelReservation(int reservationId);
    List<Reservation> getUserReservations(int userId);
    BigDecimal calculatePenalty(int reservationId);
    BigDecimal calculateReservationCost(int reservationId);
    void applyNoShowPenalties();
    void releaseExpiredReservations();
}
