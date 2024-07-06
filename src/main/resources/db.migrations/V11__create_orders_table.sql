CREATE TABLE IF NOT EXISTS orders (
    id           SERIAL PRIMARY KEY,
    user_id      INT NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    city         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    post_index   INT NOT NULL,
    total        DECIMAL(10, 2) NOT NULL,
    date_time    TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
