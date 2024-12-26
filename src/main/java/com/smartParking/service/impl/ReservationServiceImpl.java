package com.smartParking.service.impl;

import com.smartParking.WebSocketNotificationService;
import com.smartParking.dao.ReservationDAO;
import com.smartParking.dao.ParkingSpotDAO;
import com.smartParking.dao.UserDAO;
import com.smartParking.model.Reservation;
import com.smartParking.model.ParkingSpot;
import com.smartParking.model.User;
import com.smartParking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private WebSocketNotificationService notificationService;

    private static final BigDecimal PENALTY_RATE = BigDecimal.valueOf(0.50);
    private static final long MAX_RESERVATION_HOURS = 24;


    @Override
    @Transactional
    public int createReservation(Reservation reservation) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.getUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if(!user.getRole().equals("DRIVER")) {
            throw new IllegalStateException("Only Drivers can make reservations.");
        }
        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));

        if (!"AVAILABLE".equals(spot.getStatus())) {
            throw new IllegalStateException("Parking spot is not available");
        }

        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        if (hours > MAX_RESERVATION_HOURS) {
            throw new IllegalStateException("Reservations cannot exceed 24 hours.");
        }

        if (!reservationDAO.isSpotAvailableForDuration(reservation.getSpotId(),
                reservation.getStartTime(), reservation.getEndTime())) {
            throw new IllegalStateException("Spot is not available for the selected duration.");
        }
        reservation.setCost(calculateReservationCost(reservation.getStartTime(), reservation.getEndTime()));
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

        reservation.setStatus("CANCELLED");
        reservationDAO.updateReservation(reservation);
        return true;
    }

    @Override
    public List<Reservation> getUserReservations(int userId) {
        return reservationDAO.getReservationsByUserId(userId);
    }

    @Override
    public void applyOverStayPenalties() {
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> expiredReservations = reservationDAO.getExpiredReservations(now);

        for (Reservation reservation : expiredReservations) {
            ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                    .orElseThrow(() -> new IllegalStateException("Parking spot not found"));

            if (now.isAfter(reservation.getEndTime()) && spot.getStatus().equals("OCCUPIED")) {
                Duration overstay = Duration.between(reservation.getEndTime(), now);
                BigDecimal penalty = BigDecimal.valueOf(overstay.toMinutes()).multiply(BigDecimal.valueOf(0.50));
                reservation.setPenalty(penalty);
            }
        }
    }

    public BigDecimal calculateReservationCost(LocalDateTime startTime, LocalDateTime endTime) {

        Duration duration = Duration.between(startTime, endTime);
         return BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(5.00));
    }

    @Override
    public void applyNoShowPenalties() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeReservations = reservationDAO.getReservationsBySpotStatus("RESERVED");

        activeReservations.forEach(reservation -> {
            if (reservation.getEndTime().isBefore(now)) {
                BigDecimal updatedPenalty = reservation.getPenalty().add(PENALTY_RATE.multiply(BigDecimal.valueOf(20)));
                reservation.setPenalty(updatedPenalty);
                reservation.setStatus("NO_SHOW");
                reservationDAO.updateReservation(reservation);


                ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                        .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
                spot.setStatus("AVAILABLE");
                parkingSpotDAO.updateParkingSpot(spot);



                User user = userDAO.getUserById(reservation.getUserId())
                        .orElseThrow(() -> new IllegalStateException("User not found."));
                user.setTotalPenalty(user.getTotalPenalty().add(PENALTY_RATE.multiply(BigDecimal.valueOf(10))));
                userDAO.updateUser(user);

                // Notify the frontend about the penalty
                notificationService.notify(
                        "/topic/penalties",
                        reservation.getReservationId()
                );
            }
        });
    }

    @Override
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservations = reservationDAO.getExpiredReservations(now);

        for (Reservation reservation : expiredReservations) {
            ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                    .orElseThrow(() -> new IllegalStateException("Spot not found."));
            if(spot.getStatus().equals("RESERVED")) {
                spot.setStatus("AVAILABLE");
                parkingSpotDAO.updateParkingSpot(spot);

                reservation.setStatus("NO_SHOW");
                reservationDAO.updateReservation(reservation);
            }
            else if(spot.getStatus().equals("OCCUPIED")) {
                reservation.setStatus("OVER_STAY");
                reservationDAO.updateReservation(reservation);
            }
            else if(spot.getStatus().equals("AVAILABLE")) {
                reservation.setStatus("COMPLETED");
                reservationDAO.updateReservation(reservation);
            }
        }
    }

    @Override
    public void finishReservation(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
        spot.setStatus("AVAILABLE");
        parkingSpotDAO.updateParkingSpot(spot);

        reservation.setStatus("COMPLETED");
        reservationDAO.updateReservation(reservation);
    }
}