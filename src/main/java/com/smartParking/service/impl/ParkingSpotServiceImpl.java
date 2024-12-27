package com.smartParking.service.impl;

import com.smartParking.dao.ParkingSpotDAO;
import com.smartParking.dao.UserDAO;
import com.smartParking.model.ParkingSpot;
import com.smartParking.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {

    @Autowired
    private ParkingSpotDAO parkingSpotDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<ParkingSpot> getSpotsByLotId(int lotId) {
        return parkingSpotDAO.getParkingSpotsByLotId(lotId);}

}
