DROP SCHEMA IF EXISTS comment;

CREATE SCHEMA comment;

USE comment;

CREATE TABLE comment(
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    page INT NOT NULL,
    positive BOOLEAN NOT NULL,
    negative BOOLEAN NOT NULL,
    comment_text LONGTEXT NOT NULL,
    PRIMARY KEY (id)
);
