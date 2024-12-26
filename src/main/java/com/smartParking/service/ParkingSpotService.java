package com.smartParking.service;

import com.smartParking.model.ParkingSpot;
import java.util.List;

public interface ParkingSpotService {
    List<ParkingSpot> getAvailableSpots(int lotId); // For drivers
    boolean releaseSpot(int spotId); // For drivers
    boolean updateSpotStatus(int spotId, String status); // For parking managers
    List<ParkingSpot> searchSpotsByLocation(String location);
}
