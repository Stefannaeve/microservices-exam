DROP SCHEMA if EXISTS book; /* Its named after github, calm down */

CREATE SCHEMA book;

USE bookHub;

CREATE TABLE book(
    id BIGINT not null auto_increment,
    title VARCHAR(255),
    author VARCHAR(255),
    pages int,
    publish_date DATE,
    book_content LONGTEXT,

    PRIMARY KEY (id)
)