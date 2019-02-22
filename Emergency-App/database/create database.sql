DROP SCHEMA IF EXISTS inf333finalProject;
CREATE SCHEMA IF NOT EXISTS inf333finalProject;
USE inf333finalProject;

DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user(
	idUser INT AUTO_INCREMENT,
    name VARCHAR(45),
    phone CHAR(8),
    diagnose VARCHAR(45),
    CONSTRAINT PRIMARY KEY (idUser)
);

DROP TABLE IF EXISTS contact;
CREATE TABLE IF NOT EXISTS contact(
	idContact INT AUTO_INCREMENT,
    name VARCHAR(45),
    phone CHAR(8),
    idUser INT,
    CONSTRAINT PRIMARY KEY (idContact),
    CONSTRAINT FOREIGN KEY (idUser) REFERENCES user(idUser)
);

DROP TABLE IF EXISTS log;
CREATE TABLE IF NOT EXISTS log(
	idLog INT AUTO_INCREMENT,
    time DATETIME,
    location VARCHAR(45),
    CONSTRAINT PRIMARY KEY (idLog)
);

/* Data will be filled in through the app */