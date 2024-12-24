package com.smartParking.service.impl;

import com.smartParking.dao.ParkingSpotDAO;
import com.smartParking.model.ParkingSpot;
import com.smartParking.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {

    @Autowired
    private ParkingSpotDAO parkingSpotDAO;

    @Override
    public List<ParkingSpot> getAvailableSpots(int lotId) {
        return parkingSpotDAO.getParkingSpotsByLotId(lotId).stream()
                .filter(spot -> "AVAILABLE".equals(spot.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean reserveSpot(int spotId) {
        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(spotId)
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));

        if (!"AVAILABLE".equals(spot.getStatus())) {
            throw new IllegalStateException("Parking spot is not available");
        }

        spot.setStatus("RESERVED");
        return parkingSpotDAO.updateParkingSpot(spot);
    }

    @Override
    @Transactional
    public boolean releaseSpot(int spotId) {
        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(spotId)
                .orElseThrow(() -> new IllegalStateException("Parking spot not found"));

        spot.setStatus("AVAILABLE");
        return parkingSpotDAO.updateParkingSpot(spot);
    }

    @Override
    @Transactional
    public boolean updateSpotStatus(int spotId, String status) {
        ParkingSpot spot = parkingSpotDAO.getParkingSpotById(spotId)
                .orElseThrow(() -> new IllegalStateException("Parking spot not found."));
        if (!List.of("AVAILABLE", "RESERVED", "OUT_OF_SERVICE").contains(status)) {
            throw new IllegalStateException("Invalid status: " + status);
        }
        spot.setStatus(status);
        return parkingSpotDAO.updateParkingSpot(spot);
    }

    @Override
    public List<ParkingSpot> searchSpotsByLocation(String location) {
        return parkingSpotDAO.findAvailableSpotsByLocation(location);
    }
}
