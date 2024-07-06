CREATE TABLE IF NOT EXISTS cart_items (
     id             SERIAL PRIMARY KEY,
     user_id        INT NOT NULL,
     perfume_id     INT NOT NULL,
     quantity       INT NOT NULL,
     price          DECIMAL(10, 2) NOT NULL,
     perfume_title  VARCHAR(255) NOT NULL,
     perfume_brand  VARCHAR(255) NOT NULL,
     perfume_volume INT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id)
);
