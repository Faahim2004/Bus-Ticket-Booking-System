CREATE DATABASE busdb;
USE busdb;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(25) unique,
    password VARCHAR(255) NOT NULL
);



CREATE TABLE buses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bus_name VARCHAR(100),
    source VARCHAR(50),
    destination VARCHAR(50),
    total_seats INT,
    booked_seats INT DEFAULT 0
);

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    bus_id INT,
    seat_number INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ,
    FOREIGN KEY (bus_id) REFERENCES buses(id)
);


INSERT INTO users (username, email, password) VALUES ('admin','admin@gmail.com' ,'admin123');

INSERT INTO buses (bus_name, source, destination, total_seats) 
VALUES ('Express Bus', 'Thanjavur', 'Chennai', 20),
       ('FastLine', 'Trichy', 'Madurai', 20);
       
SELECT * FROM users;
SELECT * FROM buses;
SELECT * FROM tickets;

drop table users;
drop table buses;
drop table tickets;