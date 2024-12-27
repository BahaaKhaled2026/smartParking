/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { useRecoilState , useRecoilValue } from "recoil";
import { notificationsState } from '../recoil/atoms';
import { currPanel } from "../state";
import createSocket from '../Socket';

const WebSocketComponent = () => {
  const notifications = useRecoilValue(notificationsState);
  const [status, setStatus] = useState('Disconnected');
  const [panel, setPanel] = useRecoilState(currPanel);

//   useEffect(() => {
//     console.log(notifications);
//     const handleNotification = (notification) => {
//         console.log(notification);
//         setNotifications((prevNotifications) => [notification, ...prevNotifications]);
//         console.log("hii");
//     };
//     const deactivateSocket = createSocket('/topic/notification', handleNotification);
//     setStatus('Connected');
//     return () => {
//         deactivateSocket(); 
//         setStatus('Disconnected');
//     };
// }, []);

    // useEffect(() => {
    //     console.log("Notifications updated:", notifications);
    //   }, [notifications]);

  return (
    <div className={`${panel === 6 ? 'w-full lg:w-[40%] p-5' : 'w-0'} bg-white text-black overflow-hidden ease-in-out duration-300 transition-all`}>
      <h2 className="mb-2 px-2 text-2xl font-semibold font-mono tracking-tight text-primary hidden md:block">Notifications</h2> 
      <ul className='list-disc pl-5'>
        {notifications.map((notification, index) => (
          <li key={index} className='mb-2 p-2 bg-gray-100 rounded shadow'>{notification}</li>
        ))}
      </ul>
    </div>
  );
};

export default WebSocketComponent;