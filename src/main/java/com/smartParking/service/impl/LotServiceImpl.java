package com.smartParking.service.impl;

import com.smartParking.dao.ParkingLotDAO;
import com.smartParking.model.ParkingLot;
import com.smartParking.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotServiceImpl implements LotService {

    @Autowired
    private ParkingLotDAO parkingLotDAO;

    @Override
    public List<ParkingLot> getAllLots() {
        return parkingLotDAO.getAllParkingLots();
    }
}
