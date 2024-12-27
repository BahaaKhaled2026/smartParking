package com.smartParking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyPenalty(int reservationId, String message) {
        messagingTemplate.convertAndSend("/topic/penalties", new NotificationMessage(reservationId, message));
    }

    public void notifyTenMinuteLeft(int reservationId, String message) {
        messagingTemplate.convertAndSend("/topic/tenMinuteLeft", new NotificationMessage(reservationId, message));

    }


    public void testNotify(String message) {
        System.out.println("hiii: " + message);
        messagingTemplate.convertAndSend("/topic/test", new NotificationMessage(0, message));
        System.out.println("byee: " + message);
    }
}