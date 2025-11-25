-- Setup SQL script to create users table and insert initial data
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

INSERT INTO users (username, password) 
VALUES
    ('admin', '1234'), 
    ('Tyrone', 'cafe');
-- Add more initial users as needed