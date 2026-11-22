CREATE TABLE IF NOT EXISTS socks (
    id SERIAL PRIMARY KEY,
    color VARCHAR(50) NOT NULL,
    cotton_part INT NOT NULL CHECK (cotton_part BETWEEN 0 AND 100),
    UNIQUE (color, cotton_part)
);