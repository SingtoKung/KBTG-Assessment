-- Drop tables if they exist
DROP TABLE IF EXISTS lottery CASCADE;
--DROP TABLE IF EXISTS profile CASCADE;

CREATE TABLE lottery (
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    amount INT NOT NULL
);

--Initial data
INSERT INTO lottery(ticket, price, amount) VALUES('112233', 80, 1);
INSERT INTO lottery(ticket, price, amount) VALUES('445566', 80, 1);
INSERT INTO lottery(ticket, price, amount) VALUES('123456', 80, 1);
