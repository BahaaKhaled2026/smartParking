package com.smartParking.service;

import com.smartParking.model.ParkingSpot;
import java.util.List;

public interface ParkingSpotService {
    List<ParkingSpot> getSpotsByLotId(int lotId); // For drivers
}
