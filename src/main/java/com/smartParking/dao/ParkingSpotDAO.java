package com.smartParking.dao;

import com.smartParking.model.ParkingSpot;
import java.util.List;
import java.util.Optional;

public interface ParkingSpotDAO {
    int createParkingSpot(ParkingSpot parkingSpot);
    Optional<ParkingSpot> getParkingSpotById(int spotId);
    List<ParkingSpot> getParkingSpotsByLotId(int lotId);
    boolean updateParkingSpot(ParkingSpot parkingSpot);
    boolean deleteParkingSpot(int spotId);
    List<ParkingSpot> findAvailableSpotsByLocation(String location);

}
