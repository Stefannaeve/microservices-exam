DROP SCHEMA if exist bookHub; /* Its named after github, calm down */

CREATE SCHEMA bookHub;

USE bookHub;

CREATE TABLE book(
    id int not null auto_increment,
    title VARCHAR(255),
    author VARCHAR(255),
    pages int,
    publish_date DATE,
    book_content VARCHAR(999999)
)