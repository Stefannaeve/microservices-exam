CREATE DATABASE IF NOT EXISTS book;
CREATE SCHEMA IF NOT EXISTS book ;

USE book;

CREATE TABLE IF NOT EXISTS book
(
    id BIGINT not null auto_increment,
    title VARCHAR(255),
    author VARCHAR(255),
    pages int,
    publish_date DATE,
    book_content LONGTEXT,

    PRIMARY KEY (id)
)