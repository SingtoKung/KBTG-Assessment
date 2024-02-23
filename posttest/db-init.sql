-- Drop tables if they exist
DROP TABLE IF EXISTS user_ticket;
DROP TABLE IF EXISTS lottery;

CREATE TABLE lottery (
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(255) UNIQUE NOT NULL,
    price INT NOT NULL,
    amount INT NOT NULL
);

-- Create tables
CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    userId VARCHAR(255) NOT NULL,
    countTicket INT NOT NULL,
    totalCost INT NOT NULL,
    user_ticket VARCHAR(255) REFERENCES lottery(ticket) ON DELETE CASCADE
);

--Initial data
INSERT INTO lottery(ticket, price, amount) VALUES('112233', 80, 1);
INSERT INTO lottery(ticket, price, amount) VALUES('445566', 80, 2);
INSERT INTO lottery(ticket, price, amount) VALUES('778899', 80, 3);
INSERT INTO lottery(ticket, price, amount) VALUES('123456', 80, 2);
