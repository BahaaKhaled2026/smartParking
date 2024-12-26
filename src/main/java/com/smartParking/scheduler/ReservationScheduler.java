package com.smartParking.scheduler;

import com.smartParking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReservationScheduler {

    @Autowired
    private ReservationService reservationService;

    @Scheduled(fixedRate = 60000)
    public void processPenaltiesAndReleases() {
        reservationService.applyNoShowPenalties();
        reservationService.applyOverStayPenalties();
        reservationService.releaseExpiredReservations();
    }
}
