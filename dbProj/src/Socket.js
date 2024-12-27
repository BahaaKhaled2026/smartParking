import * as Stomp from '@stomp/stompjs';

const createSocket = (path, onMessageReceived) => {
    const stompClient = new Stomp.Client({
        brokerURL: 'ws://localhost:8080/ws', 
        reconnectDelay: 2000, 
        onConnect: () => {
            console.log('WebSocket connection established');
            console.log('Subscribing to:', path);
            
            stompClient.subscribe(path, (message) => {
                console.log('Message received:', message.body);
                console.log("hiiiiiii")
                const notification = JSON.parse(message.body);
                onMessageReceived(notification); 
                console.log(notification);

            });
        },
        onStompError: (frame) => {
            console.error('Broker reported error: ', frame.headers['message']);
            console.error('Additional details: ', frame.body);
        },
        onDisconnect: () => {
            console.log('WebSocket connection closed');
        },
    });

    stompClient.activate(); 

    return () => {
        if (stompClient.active) {
            stompClient.deactivate();
            console.log('WebSocket connection deactivated');
        }
    };
};

export default createSocket;