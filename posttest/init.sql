-- Drop tables if they exist
DROP TABLE IF EXISTS lottery CASCADE;
DROP TABLE IF EXISTS user_ticket CASCADE;

--DROP TABLE IF EXISTS profile CASCADE;
CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    userId VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE lottery (
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(255) UNIQUE NOT NULL,
    price INT NOT NULL,
    amount INT NOT NULL,
    user_ticket VARCHAR REFERENCES user_ticket(userId) ON DELETE CASCADE

);

--Initial data
INSERT INTO user_ticket(userId) VALUES('1234567890');
INSERT INTO user_ticket(userId) VALUES('0987654321');
INSERT INTO lottery(ticket, price, amount, user_ticket) VALUES('112233', 80, 1, '1234567890');
INSERT INTO lottery(ticket, price, amount, user_ticket) VALUES('445566', 80, 2, '1234567890');
INSERT INTO lottery(ticket, price, amount) VALUES('123456', 80, 1);
INSERT INTO lottery(ticket, price, amount) VALUES('234568', 80, 3);
