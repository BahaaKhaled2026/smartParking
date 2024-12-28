package com.smartParking.service.impl;

import com.smartParking.dao.ParkingLotDAO;
import com.smartParking.dao.ParkingSpotDAO;
import com.smartParking.dao.UserDAO;
import com.smartParking.model.ParkingLot;
import com.smartParking.model.ParkingSpot;
import com.smartParking.model.User;
import com.smartParking.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotServiceImpl implements LotService {

    @Autowired
    private ParkingLotDAO parkingLotDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private ParkingSpotDAO parkingSpotDAO;

    @Override
    public List<ParkingLot> getAllLots() {
        return parkingLotDAO.getAllParkingLots();
    }

    @Override
    public List<ParkingLot> getLotsByManagerId(int managerId) {
        return parkingLotDAO.getParkingLotsByManagerId(managerId);
    }

    @Override
    public void adminAddLot(ParkingLot parkingLot) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.getUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if(!user.getRole().equals("ADMIN")){
            throw new IllegalStateException("Only admin can add lots.");
        }
        parkingLotDAO.createParkingLot(parkingLot);
        for (int i = 1; i <= parkingLot.getCapacity(); i++) {
            String spotNumber = "S" + i;
            parkingSpotDAO.createParkingSpot(new ParkingSpot(parkingLot.getLotId(), spotNumber, "REGULAR", "AVAILABLE"));
        }

        notificationService.notify(parkingLot.getLotId(), "Your Lot Has Added", user.getUserId());
    }
}
