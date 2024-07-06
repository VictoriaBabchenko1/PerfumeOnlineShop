CREATE TABLE IF NOT EXISTS perfumes (
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(100) NOT NULL,
    brand           VARCHAR(60) NOT NULL,
    year            INTEGER NOT NULL,
    country         VARCHAR(60) NOT NULL,
    description     VARCHAR(1000),
    gender          VARCHAR(60) NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    type            VARCHAR(60) NOT NULL,
    volume          INTEGER NOT NULL,
    fragrance_notes VARCHAR(255) NOT NULL
);
