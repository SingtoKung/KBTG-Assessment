-- Drop tables if they exist
DROP TABLE IF EXISTS user_ticket;
DROP TABLE IF EXISTS lottery;
DROP TABLE IF EXISTS user_ticket_mapping;

-- Create tables
CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    userId VARCHAR(255) UNIQUE NOT NULL,
    countTicket INT NOT NULL,
    totalCost INT NOT NULL
);

CREATE TABLE lottery (
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(255) UNIQUE NOT NULL,
    price INT NOT NULL,
    amount INT NOT NULL
);

CREATE TABLE user_ticket_mapping (
    user_id VARCHAR(255) NOT NULL,
    lottery_id INT NOT NULL
);

--Initial data
INSERT INTO user_ticket(userId, countTicket, totalCost) VALUES('1234567890', 0, 0);
INSERT INTO user_ticket(userId, countTicket, totalCost) VALUES('0987654321', 0, 0);
INSERT INTO lottery(ticket, price, amount) VALUES('112233', 80, 1);
INSERT INTO lottery(ticket, price, amount) VALUES('445566', 80, 2);
INSERT INTO lottery(ticket, price, amount) VALUES('778899', 80, 3);
