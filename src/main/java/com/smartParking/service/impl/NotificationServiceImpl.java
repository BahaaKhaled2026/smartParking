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

    public void sendNotification(String message){
        System.out.println("NotificationServiceImpl1: " + message);
        messagingTemplate.convertAndSend("/topic/notification", message);
        System.out.println("NotificationServiceImpl2: " + message);
    }
}
