package com.smartParking.service.impl;

import com.smartParking.dao.ReservationDAO;
import com.smartParking.dao.ParkingSpotDAO;
import com.smartParking.model.Reservation;
import com.smartParking.model.ParkingSpot;
import com.smartParking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    @Autowired
    private ParkingSpotDAO parkingSpotDAO;

    @Override
    @Transactional
    public int createReservation(Reservation reservation) {
        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));

        if (!"AVAILABLE".equals(spot.getStatus())) {
            throw new IllegalStateException("Parking spot is not available");
        }

        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        if (hours > 24) {
            throw new IllegalStateException("Reservations cannot exceed 24 hours.");
        }

        if (!reservationDAO.isSpotAvailableForDuration(reservation.getSpotId(),
                reservation.getStartTime(), reservation.getEndTime())) {
            throw new IllegalStateException("Spot is not available for the selected duration.");
        }

        int reservationId = reservationDAO.createReservation(reservation);
        spot.setStatus("RESERVED");
        parkingSpotDAO.updateParkingSpot(spot);

        return reservationId;
    }

    @Override
    @Transactional
    public boolean cancelReservation(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
        spot.setStatus("AVAILABLE");
        parkingSpotDAO.updateParkingSpot(spot);

        return reservationDAO.deleteReservation(reservationId);
    }

    @Override
    public List<Reservation> getUserReservations(int userId) {
        return reservationDAO.getReservationsByUserId(userId);
    }

    @Override
    public BigDecimal calculatePenalty(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(reservation.getEndTime())) {
            Duration overstay = Duration.between(reservation.getEndTime(), now);
            return BigDecimal.valueOf(overstay.toMinutes()).multiply(BigDecimal.valueOf(0.50));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateReservationCost(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        Duration duration = Duration.between(reservation.getStartTime(), reservation.getEndTime());
        return BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(5.00));
    }

    @Override
    public void applyNoShowPenalties() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeReservations = reservationDAO.getReservationsByStatus("RESERVED");

        for (Reservation reservation : activeReservations) {
            if (reservation.getStartTime().isBefore(now)) {
                reservation.setPenalty(reservation.getPenalty().add(BigDecimal.valueOf(10.00)));
                reservationDAO.updateReservation(reservation);
            }
        }
    }

    @Override
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservations = reservationDAO.getExpiredReservations(now);

        for (Reservation reservation : expiredReservations) {
            ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                    .orElseThrow(() -> new IllegalStateException("Spot not found."));
            spot.setStatus("AVAILABLE");
            parkingSpotDAO.updateParkingSpot(spot);

            reservation.setStatus("EXPIRED");
            reservationDAO.updateReservation(reservation);
        }
    }


}