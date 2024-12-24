package com.smartParking.dao;

import com.smartParking.model.ParkingLot;
import java.util.List;
import java.util.Optional;

public interface ParkingLotDAO {
    int createParkingLot(ParkingLot parkingLot); // Returns the generated ID
    Optional<ParkingLot> getParkingLotById(int lotId);
    List<ParkingLot> getAllParkingLots();
    boolean updateParkingLot(ParkingLot parkingLot);
    boolean deleteParkingLot(int lotId);
}
