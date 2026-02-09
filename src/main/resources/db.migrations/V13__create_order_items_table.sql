CREATE TABLE IF NOT EXISTS order_items (
    id             SERIAL PRIMARY KEY,
    order_id       INT NOT NULL,
    perfume_id     INT NOT NULL,
    quantity       INT NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    perfume_title  VARCHAR(255) NOT NULL,
    perfume_brand  VARCHAR(255) NOT NULL,
    perfume_volume INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (perfume_id) REFERENCES perfumes(id)
);


