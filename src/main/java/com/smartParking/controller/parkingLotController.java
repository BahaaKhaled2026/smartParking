package com.smartParking.controller;

import com.smartParking.model.ParkingLot;
import com.smartParking.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lots")
public class parkingLotController {

    @Autowired
    private LotService lotService;

    @GetMapping("/getlots")
    public ResponseEntity<?> getManagerLots() {
        try {
            return ResponseEntity.ok(lotService.getAllLots());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getmanagerlots")
    public ResponseEntity<?> getManagerLots(@RequestParam("managerId") int managerId) {
        try {
            return ResponseEntity.ok(lotService.getLotsByManagerId(managerId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/addlot")
    public ResponseEntity<?> addLot(@RequestBody ParkingLot parkingLot) {
        try {
            lotService.adminAddLot(parkingLot);
            return ResponseEntity.ok("Parking lot added successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}