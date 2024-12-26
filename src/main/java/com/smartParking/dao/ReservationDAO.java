package com.smartParking.dao;

import com.smartParking.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {
    int createReservation(Reservation reservation); // Returns the generated ID
    Optional<Reservation> getReservationById(int reservationId);
    List<Reservation> getReservationsByUserId(int userId);
    List<Reservation> getReservationsBySpotId(int spotId);
    boolean updateReservation(Reservation reservation);
    boolean deleteReservation(int reservationId);
    boolean isSpotAvailableForDuration(int spotId, LocalDateTime startTime, LocalDateTime endTime);
    List<Reservation> getExpiredReservations(LocalDateTime currentTime);
    List<Reservation> getReservationsByStatus(String status);
    List<Reservation> getReservationsBySpotStatus(String status);
}
