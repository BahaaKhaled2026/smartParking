package com.smartParking.service;
import com.smartParking.model.ParkingLot;

import java.util.List;

public interface LotService {
    List<ParkingLot> getAllLots();
    List<ParkingLot> getLotsByManagerId(int managerId);
}
