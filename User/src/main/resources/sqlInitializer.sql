DROP SCHEMA if EXISTS user;

CREATE SCHEMA user;

USE user;



CREATE TABLE user(
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),

    PRIMARY KEY (id)
);
CREATE TABLE book_Id(
    id BIGINT NOT NULL AUTO_INCREMENT,
#     user_id BIGINT,

    PRIMARY KEY(id)

);
# create table user_books
# (
#     user_id BIGINT not null,
#     book_id BIGINT not null,
#     constraint book_id
#         foreign key (book_id) references user.book_Id (id),
#     constraint user_id
#         foreign key (user_id) references user.user (id)
# );

