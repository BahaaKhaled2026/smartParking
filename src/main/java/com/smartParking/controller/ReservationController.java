package com.smartParking.controller;

import com.smartParking.model.Reservation;
import com.smartParking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> reserveSpotForDuration(
            @RequestBody Reservation reservation,
            @RequestParam("reserve") boolean reserve) {
        try {
            if (reserve) {
                int reservationId = (int) reservationService.createReservation(reservation, reserve);
                return ResponseEntity.ok(Map.of(
                        "message", "Reservation created successfully",
                        "reservationId", reservationId
                ));
            } else {
                BigDecimal cost = (BigDecimal) reservationService.createReservation(reservation, reserve);
                return ResponseEntity.ok(Map.of(
                        "message", "Reservation cost calculated",
                        "cost", cost
                ));
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }


    @DeleteMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelReservation(@RequestParam("reservationId") int reservationId) {
        System.out.println("y3m ya ya sa7eb el gamal");
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok(Map.of(
                    "message", "Reservation cancelled successfully"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/finish")
    public ResponseEntity<Map<String, Object>>finishReservation(@RequestParam("reservationId") int reservationId) {
        try {
            reservationService.finishReservation(reservationId);
            return ResponseEntity.ok(Map.of(
                    "message", "Reservation finished successfully"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/arrive")
    public ResponseEntity<Map<String, Object>> arriveAtSpot(@RequestParam("reservationId") int reservationId) {
        try {
            reservationService.arriveAtSpot(reservationId);
            return ResponseEntity.ok(Map.of(
                    "message", "Arrived at spot successfully"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Reservation>> getUserReservations(@RequestParam("userId") int userId) {
        List<Reservation> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

}
