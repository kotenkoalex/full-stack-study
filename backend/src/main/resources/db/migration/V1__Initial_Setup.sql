CREATE TABLE customer
(
    id    SERIAL PRIMARY KEY ,
    name  TEXT NOT NULL ,
    email VARCHAR(50) NOT NULL ,
    age   INT NOT NULL
);