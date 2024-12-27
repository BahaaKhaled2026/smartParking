/* eslint-disable react/prop-types */
import React, { createContext, useContext, useEffect, useRef } from 'react';
import { currUser } from "../state";
import { useRecoilState } from 'recoil';
import { notificationsState } from '../recoil/atoms'; // Create a Recoil atom for notifications.
import { Client } from '@stomp/stompjs';

const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const socketRef = useRef(null);
    const[user,setUser]=useRecoilState(currUser);
  const [notifications, setNotifications] = useRecoilState(notificationsState);

  useEffect(() => {
    const handleNotification = (message) => {
      setNotifications((prev) => [message.body, ...prev]); // Assuming the message is in `message.body`
    };

    // Create the WebSocket connection

    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      onConnect: () => {
        const userId = user ? user.userId : "";
        console.log(userId);
        client.subscribe(`/topic/notification/${userId}`, handleNotification);
      },
      onDisconnect: () => {
        console.log('WebSocket disconnected.');
      },
    });

    client.activate();
    socketRef.current = client;

    // Cleanup on unmount
    return () => {
      client.deactivate();
    };
  }, [setNotifications]);

  return (
    <WebSocketContext.Provider value={socketRef.current}>
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => useContext(WebSocketContext);
