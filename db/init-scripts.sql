create extension hstore;
create schema cabs;

--FOR USERS
create table if not exists cabs."Users"("user_id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"last_name" VARCHAR NOT NULL,"email" VARCHAR NOT NULL,"password" VARCHAR NOT NULL);

--FOR CABS

-- CAB DETAILS TABLE --

CREATE TABLE IF NOT EXISTS cabs."Cab_details" (
    "cab_id" SERIAL PRIMARY KEY,
    "cab_name" VARCHAR NOT NULL,
    "fare" DECIMAL NOT NULL,
    "seater" INTEGER NOT NULL,
    "driver_id" SERIAL ,
    "driver_name" VARCHAR NOT NULL,
    "status" BOOLEAN NOT NULL DEFAULT false
);

-- DRIVER TABLE --

CREATE TABLE IF NOT EXISTS cabs."Driver" (
    "driver_id" SERIAL PRIMARY KEY,
    "driver_email" VARCHAR NOT NULL,
    "driver_password" VARCHAR NOT NULL,
    "driver_name" VARCHAR NOT NULL,
    "cab_name" VARCHAR NOT NULL,
    "public_key" VARCHAR NOT NULL
);


-- RATINGS TABLE
CREATE TABLE cabs."Ratings"(
    "rating_id" BIGSERIAL NOT NULL PRIMARY KEY,
    "cab_id" INT NOT NULL,
    "from_city_id" INT NOT NULL,
    "to_city_id" INT NOT NULL,
    "rating" INT NOT NULL
);

-- Create the 'Bookings' table
CREATE TABLE IF NOT EXISTS cabs."Bookings"("booking_id" SERIAL NOT NULL PRIMARY KEY,"user_id" VARCHAR NOT NULL,"cab" VARCHAR NOT NULL,"booking_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"from_city_id" INT NOT NULL,"to_city_id" INT NOT NULL,"cab_id" INT NOT NULL,"fare" INT NOT NULL);

--INSERT INTO cabs."Cab_details"("cab_name", "fare", "seater","driver_id", "driver_name", "status")
--VALUES
--    ('Toyota Innova', 200.00, 7,4223, 'John Doe', false),
--    ('Honda City', 150.00, 4,2323, 'Jane Smith', false),
--    ('Maruti Swift', 120.00, 5,123, 'David Johnson', false),
--    ('Hyundai Creta', 180.00, 5, 213,'Emily Brown', false),
--    ('Ford EcoSport', 160.00, 5, 1212,'Michael Wilson', false),
--    ('Chevrolet Beat', 100.00, 4, 1231,'Sarah Garcia', false);


-- Create the 'cities' table
CREATE TABLE cabs."Cities" (
    city_id INT PRIMARY KEY,
    city_name VARCHAR(255)
);

-- Create the 'distances' table
CREATE TABLE cabs."Distances"(
    distance_id INT PRIMARY KEY,
    from_city_id INT,
    to_city_id INT,
    distance_km DECIMAL(10, 2),
    FOREIGN KEY (from_city_id) REFERENCES cabs."Cities"(city_id),
    FOREIGN KEY (to_city_id) REFERENCES cabs."Cities"(city_id)
);

-- Insert data into the 'cities' table for five cities
INSERT INTO cabs."Cities" (city_id, city_name) VALUES
(1, 'New York'),
(2, 'Los Angeles'),
(3, 'Chicago'),
(4, 'Houston'),
(5, 'Phoenix');

-- Insert data into the 'distances' table for distances between all five cities
INSERT INTO cabs."Distances" (distance_id, from_city_id, to_city_id, distance_km) VALUES
(1, 1, 2, 399), -- New York to Los Angeles
(2, 1, 3, 145), -- New York to Chicago
(3, 1, 4, 279), -- New York to Houston
(4, 1, 5, 380), -- New York to Phoenix
(5, 2, 3, 241), -- Los Angeles to Chicago
(6, 2, 4, 211), -- Los Angeles to Houston
(7, 2, 5, 57),  -- Los Angeles to Phoenix
(8, 3, 4, 155),  -- Chicago to Houston
(9, 3, 5, 276),  -- Chicago to Phoenix
(10, 4, 5, 482);  -- Houston to Phoenix
