package com.smartParking.controller;

import com.smartParking.model.ParkingSpot;
import com.smartParking.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @GetMapping("/available/{lotId}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots(@PathVariable int lotId) {
        List<ParkingSpot> availableSpots = parkingSpotService.getAvailableSpots(lotId);
        return ResponseEntity.ok(availableSpots);
    }

    @PostMapping("/reserve/{spotId}")
    public ResponseEntity<String> reserveSpot(@PathVariable int spotId) {
        try {
            parkingSpotService.reserveSpot(spotId);
            return ResponseEntity.ok("Spot reserved successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/release/{spotId}")
    public ResponseEntity<String> releaseSpot(@PathVariable int spotId) {
        try {
            parkingSpotService.releaseSpot(spotId);
            return ResponseEntity.ok("Spot released successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/manager/update-status/{spotId}")
    public ResponseEntity<String> updateSpotStatus(@PathVariable int spotId, @RequestParam String status) {
        try {
            parkingSpotService.updateSpotStatus(spotId, status);
            return ResponseEntity.ok("Spot status updated successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParkingSpot>> searchSpotsByLocation(@RequestParam String location) {
        List<ParkingSpot> spots = parkingSpotService.searchSpotsByLocation(location);
        return ResponseEntity.ok(spots);
    }

}
