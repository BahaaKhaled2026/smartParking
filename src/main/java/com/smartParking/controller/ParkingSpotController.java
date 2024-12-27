package com.smartParking.controller;

import com.smartParking.model.ParkingSpot;
import com.smartParking.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spots")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @GetMapping("/getspots")
    public ResponseEntity<?> getSpotsByLotId(@RequestParam("lotId") int lotId) {
        try {
            return ResponseEntity.ok(parkingSpotService.getSpotsByLotId(lotId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
