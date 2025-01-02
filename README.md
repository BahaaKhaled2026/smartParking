# Smart Parking
## Overview
Smart Parking is a modern parking management system designed to streamline the process of finding and reserving parking spaces. It leverages advanced technologies to provide a seamless and efficient parking experience for users.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Technologies Used](#technologies-used)
- [Dependencies](#dependencies)


## Features
- **Real-time Availability**: Check the availability of parking spaces in real-time.
- **Reservation System**: Reserve parking spots in advance.
- **User Authentication**: Secure login and registration for users.
- **Admin Dashboard**: Manage parking spaces, view analytics, and handle user queries.
- **Mobile Compatibility**: Access smartParking on the go with our mobile application.
- **Notifications**: Receive notifications for reservation confirmations and reminders.
- **Parking History**: View your past parking reservations and payments.
- **Interactive Maps**: View parking locations on an interactive map.

## Installation
To get started with smartParking, follow these steps:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/BahaaKhaled2026/smartParking.git
    cd smartParking
    ```

2. **Install dependencies**:
    ```bash
    npm install
    ```


4. **Start the development server**:
    ```bash
    npm start
    ```

5. **Build the backend**:
    ```bash
    ./mvnw clean install
    ```
6. **Run the backend**:
    ```bash
    ./mvnw spring-boot:run
    ```

## Usage
- **User**: Sign up or log in to your account, search for available parking spaces, and make reservations.
- **Admin**: Log in to the admin dashboard to manage parking spaces, view user activity, and access analytics.

## API Endpoints
- **GET /api/parking**: Retrieve a list of available parking spaces.
- **POST /api/parking/reserve**: Reserve a parking space.
- **POST /api/payment**: Process a payment.

## Technologies Used
- **Frontend**: React with JavaScript & Tailwind-CSS
- **Backend**: SpringBoot, Java
- **Database**: MySQL with JDBC
- **Authentication**: JWT

## Dependencies

### Backend
- **Spring Boot**: A framework for building Java-based applications.
- **Spring Data JDBC**: A module for accessing relational databases using JDBC.
- **Spring Security**: A framework for securing Java applications.
- **Spring WebSocket**: A module for WebSocket communication.
- **MySQL Connector/J**: A JDBC driver for MySQL.
- **JJWT**: A library for creating and verifying JSON Web Tokens (JWT).
- **JasperReports**: A reporting library for generating reports.

### Frontend
- **React**: A JavaScript library for building user interfaces.
- **Tailwind CSS**: A utility-first CSS framework.
- **Vite**: A build tool for frontend projects.
- **Recoil**: A state management library for React.
- **React Router**: A library for routing in React applications.
- **React Toastify**: A library for displaying toast notifications in React.
- **Leaflet**: A library for interactive maps.
- **Firebase**: A platform for building web and mobile applications.
- **StompJS**: A library for STOMP messaging over WebSocket.
