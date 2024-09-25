DROP SCHEMA IF EXISTS comment;

CREATE SCHEMA comment;

USE comment;

CREATE TABLE comment(
    id BIGINT NOT NULL AUTO_INCREMENT,
    page INT NOT NULL,
    comment_text LONGTEXT NOT NULL,
    PRIMARY KEY (id)
);
