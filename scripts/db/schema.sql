DROP DATABASE IF EXISTS Cardvert;
CREATE DATABASE Cardvert;
USE Cardvert;

DROP TABLE IF EXISTS Adverts;
CREATE TABLE Adverts(
  id           int PRIMARY KEY,
  title        varchar(100),
  fuel         smallint,
  price        int,
  is_new       bit,
  mileage      int,
  registration date
);

INSERT INTO Adverts (id, title, fuel, price, is_new, mileage, registration)
VALUES (1, 'New Toyota Kijang', 1, 92000, 1, null, null);

INSERT INTO Adverts (id, title, fuel, price, is_new, mileage, registration)
VALUES (2, 'Used Toyota Kijang', 1, 52000, 0, 889, '2014-12-2');

INSERT INTO Adverts (id, title, fuel, price, is_new, mileage, registration)
VALUES (3, 'Brand New Maybach', 1, 660500, 1, null, null);

INSERT INTO Adverts (id, title, fuel, price, is_new, mileage, registration)
VALUES (4, 'Mr. Beans'' Orange 1969 Mini', 1, 550000, 0, 1000, '2005-5-5');

INSERT INTO Adverts (id, title, fuel, price, is_new, mileage, registration)
VALUES (5, 'New Isuzu Panther Diesel', 0, 6000, 1, null, null);
