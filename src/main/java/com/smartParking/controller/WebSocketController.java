package com.smartParking.controller;

import com.smartParking.NotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendTestMessage")
    @SendTo("/topic/test")
    public NotificationMessage sendTestMessage(NotificationMessage message) {
        return new NotificationMessage(message.getReservationId(), "Test Message: " + message.getMessage());
    }


    @MessageMapping("/sendPenalty")
    @SendTo("/topic/penalties")
    public NotificationMessage sendPenalty(NotificationMessage message) {
        return new NotificationMessage(message.getReservationId(), "Penalty: " + message.getMessage());
    }
}
