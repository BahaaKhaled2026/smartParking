package com.smartParking.service.impl;

import com.smartParking.WebSocketNotificationService;
import com.smartParking.dao.ParkingLotDAO;
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
    private ParkingLotDAO parkingLotDAO;

    @Autowired
    private WebSocketNotificationService notificationService;

    private static final BigDecimal PENALTY_RATE = BigDecimal.valueOf(0.1);
    private static final long MAX_RESERVATION_HOURS = 24;
    private static final long MIN_RESERVATION_HOURS = 1;


    @Override
    @Transactional
    public Number createReservation(Reservation reservation , boolean reserve) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.getUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if(!user.getRole().equals("DRIVER")) {
            throw new IllegalStateException("Only Drivers can make reservations.");
        }
        if(reservation.getStartTime() == null || reservation.getEndTime() == null) {
            throw new IllegalStateException("Start time and end time must be provided.");
        }
        System.out.println("start time: " + reservation.getStartTime());
        System.out.println("end time: " + reservation.getEndTime());

        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        if (hours > MAX_RESERVATION_HOURS) {
            throw new IllegalStateException("Reservations cannot exceed 24 hours.");
        }
        if (hours < MIN_RESERVATION_HOURS) {
            throw new IllegalStateException("Reservations must be at least 1 hour.");
        }
        if(reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reservations cannot be made in the past.");
        }
        if(reservation.getEndTime().isBefore(reservation.getStartTime())) {
            throw new IllegalStateException("End time must be after start time.");
        }

        if (!reservationDAO.isSpotAvailableForDuration(reservation.getSpotId(),
                reservation.getStartTime(), reservation.getEndTime())) {
            throw new IllegalStateException("Spot is not available for the selected duration.");
        }

        int lotId = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"))
                .getLotId();
        reservation.setCost(calculateReservationCost(reservation.getStartTime(), reservation.getEndTime() , lotId));
        if(reserve){
            if(reservation.getCost().compareTo(user.getBalance()) > 0) {
                throw new IllegalStateException("Insufficient balance.");
            }
            user.setBalance(user.getBalance().subtract(reservation.getCost()));
            userDAO.updateUser(user);
            parkingLotDAO.updateTotalRevenue(lotId, reservation.getCost());
            return reservationDAO.createReservation(reservation);
        }
        System.out.println("cost: " + reservation.getCost());
        return reservation.getCost();
    }

    @Override
    @Transactional
    public boolean cancelReservation(int reservationId) {
        System.out.println("yarab2");
        Reservation reservation = reservationDAO.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
        spot.setStatus("AVAILABLE");
        parkingSpotDAO.updateParkingSpot(spot);

        reservation.setStatus("CANCELLED");
        reservationDAO.updateReservation(reservation);

        User user = userDAO.getUserById(reservation.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found."));
        user.setBalance(user.getBalance().add(reservation.getCost()));
        BigDecimal penalty = reservation.getCost().multiply(PENALTY_RATE);
        user.setTotalPenalty(user.getTotalPenalty().add(penalty));
        notificationService.notifyPenalty(reservation.getReservationId(), "You have been charged a penalty of $" + penalty + " for cancelling your reservation.");
        userDAO.updateUser(user);

        int lotId = spot.getLotId();
        parkingLotDAO.updateTotalRevenue(lotId, reservation.getCost().multiply(BigDecimal.valueOf(-1)));
        parkingLotDAO.updateTotalPenalty(lotId, penalty);
        parkingSpotDAO.updateParkingSpot(spot);

        return true;
    }

    @Override
    public List<Reservation> getUserReservations(int userId) {
        return reservationDAO.getReservationsByUserId(userId);
    }

    @Override
    public void applyOverStayPenalties(Reservation reservation) {
        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
        BigDecimal penalty = reservation.getCost().multiply(PENALTY_RATE);
        reservation.setPenalty(penalty);
        reservation.setStatus("COMPLETED");
        reservationDAO.updateReservation(reservation);
        spot.setStatus("AVAILABLE");
        parkingSpotDAO.updateParkingSpot(spot);

        User user = userDAO.getUserById(reservation.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found."));
        user.setTotalPenalty(user.getTotalPenalty().add(penalty));
        userDAO.updateUser(user);

        int lotId = spot.getLotId();
        parkingLotDAO.updateTotalPenalty(lotId, penalty);
        notificationService.notifyPenalty(reservation.getReservationId(), "You have been charged a penalty of $" + penalty + " for overstaying your reservation.");
    }

    public BigDecimal calculateReservationCost(LocalDateTime startTime, LocalDateTime endTime , int lotId) {
        int startHour = startTime.getHour();
        int endHour = endTime.getHour();
        // calculate different cost based on the time of the day
        BigDecimal cost = BigDecimal.ZERO;
        if (startHour >= 12 && startHour < 18 && endHour >= 12 && endHour < 18) {
            cost = BigDecimal.valueOf(Duration.between(startTime, endTime).toMinutes()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(5.00));
        } else {
            cost = BigDecimal.valueOf(Duration.between(startTime, endTime).toMinutes()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(3.00));
        }
        List<ParkingSpot> spots = parkingSpotDAO.getParkingSpotsByLotId(lotId);
        int availableSpots = 0;
        for (ParkingSpot spot : spots) {
            if (spot.getStatus().equals("AVAILABLE")) {
                availableSpots++;
            }
        }
        double percentage = (double) availableSpots / spots.size();
        if (percentage < 0.1) {
            cost = cost.multiply(BigDecimal.valueOf(1.5));
        }
        return cost;
    }

    @Override
    public void applyNoShowPenalties() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeReservations = reservationDAO.getReservationsBySpotStatus("RESERVED");

        activeReservations.forEach(reservation -> {
            if (reservation.getEndTime().isBefore(now)) {
                BigDecimal penalty = reservation.getCost().multiply(PENALTY_RATE);
                reservation.setPenalty(penalty);
                reservation.setStatus("NO_SHOW");
                reservationDAO.updateReservation(reservation);


                ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                        .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
                spot.setStatus("AVAILABLE");
                parkingSpotDAO.updateParkingSpot(spot);


                User user = userDAO.getUserById(reservation.getUserId())
                        .orElseThrow(() -> new IllegalStateException("User not found."));
                user.setTotalPenalty(user.getTotalPenalty().add(penalty));
                userDAO.updateUser(user);

                int lotId = spot.getLotId();
                parkingLotDAO.updateTotalPenalty(lotId, penalty);

                notificationService.notifyPenalty(reservation.getReservationId(), "You have been charged a penalty of $" + penalty + " for not showing up to your reservation.");
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
                applyOverStayPenalties(reservation);
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

    @Override
    public void startReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeReservations = reservationDAO.getStartedReservations(now);

        for (Reservation reservation : activeReservations) {
            ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                    .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
            spot.setStatus("RESERVED");
            parkingSpotDAO.updateParkingSpot(spot);
        }
    }

    @Override
    public void arriveAtSpot(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(reservation.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));
        spot.setStatus("OCCUPIED");
        parkingSpotDAO.updateParkingSpot(spot);
    }

    @Override
    public void tenMinuteLeftNotification() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeReservations = reservationDAO.getReservationsByStatus("ACTIVE");

        for (Reservation reservation : activeReservations) {
            if (Duration.between(now, reservation.getEndTime()).toMinutes() == 10) {
                notificationService.notifyTenMinuteLeft(reservation.getReservationId(), "You have 10 minutes left before your reservation ends.");
            }
        }
    }
}
