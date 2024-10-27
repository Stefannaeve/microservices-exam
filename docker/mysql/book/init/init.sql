DROP SCHEMA if EXISTS book;

CREATE SCHEMA book;

USE book;

CREATE TABLE book(
    id BIGINT not null auto_increment,
    title VARCHAR(255),
    author VARCHAR(255),
    pages int,
    publish_date DATE,
    book_content LONGTEXT,

    PRIMARY KEY (id)
)