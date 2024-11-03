CREATE DATABASE IF NOT EXISTS user;
DROP SCHEMA if EXISTS user;

CREATE SCHEMA user;

USE user;

CREATE TABLE user
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),

    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE book_Id
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    pages           int,
    readingProgress VARCHAR(20),
    readingStatus   ENUM ('NotYetStarted', 'InProgress', 'Finished', 'DidNotFinish', 'ToBeRead'),

    PRIMARY KEY (id)
);