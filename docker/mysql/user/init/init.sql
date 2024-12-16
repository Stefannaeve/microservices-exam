CREATE DATABASE IF NOT EXISTS user;
CREATE SCHEMA IF NOT EXISTS user;

USE user;

CREATE TABLE IF NOT EXISTS user
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),

    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS book_id
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    pages           int,
    readingProgress VARCHAR(20),
    readingStatus   ENUM ('NotYetStarted', 'InProgress', 'Finished', 'DidNotFinish', 'ToBeRead'),

    PRIMARY KEY (id)
);