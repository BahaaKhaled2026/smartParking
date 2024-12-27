CREATE DATABASE smart_city_parking;
SHOW DATABASES;
USE smart_city_parking;


CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('DRIVER', 'MANAGER', 'ADMIN') NOT NULL,
    email VARCHAR(100) UNIQUE,
    license_plate VARCHAR(20), -- Only for drivers
	total_penalty DECIMAL(10, 2) DEFAULT 0.00, -- For no-show or overstay
    balance DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parking_lots (
    lot_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    longitude DECIMAL(65, 30) NOT NULL,
    latitude DECIMAL(65, 30) NOT NULL,
    capacity INT NOT NULL, -- Total number of spots
	total_revenue DECIMAL(10, 2) DEFAULT 0.00,
	total_penalty DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE parking_spots (
    spot_id INT AUTO_INCREMENT PRIMARY KEY,
    lot_id INT NOT NULL,
    spot_number VARCHAR(10) NOT NULL, -- Unique per lot
    type ENUM('REGULAR', 'DISABLED', 'EV') NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED') NOT NULL DEFAULT 'AVAILABLE',
    FOREIGN KEY (lot_id) REFERENCES parking_lots(lot_id) ON DELETE CASCADE
);


CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    spot_id INT NOT NULL,
    spot_number VARCHAR(10) NOT NULL,
    lot_id INT NOT NULL,
    lot_name VARCHAR(100) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED' , 'NO_SHOW') NOT NULL DEFAULT 'ACTIVE',
    penalty DECIMAL(10, 2) DEFAULT 0.00, -- For no-show or overstay
	cost DECIMAL(10, 2) DEFAULT 0.00, 
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id) ON DELETE CASCADE,
    FOREIGN KEY (lot_id) REFERENCES parking_lots(lot_id) ON DELETE CASCADE
);

SELECT user, host, plugin FROM mysql.user WHERE user = 'root';

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'smart_city_parking';
FLUSH PRIVILEGES;
