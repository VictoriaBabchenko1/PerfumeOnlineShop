CREATE TABLE IF NOT EXISTS users (
    id                  SERIAL PRIMARY KEY,
    username            VARCHAR(100) UNIQUE NOT NULL,
    email               VARCHAR(100) UNIQUE NOT NULL,
    password            VARCHAR(100) NOT NULL,
    token               VARCHAR(255),
    token_creation_date TIMESTAMP
);
