package com.smartParking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl {
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired

    public NotificationServiceImpl(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public void notify(int reservationId, String s , int userId) {
        messagingTemplate.convertAndSend("/topic/notification/"+userId, s);
    }
}
