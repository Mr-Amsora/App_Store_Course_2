INSERT INTO users (id, name, email, password)
VALUES (
           1,
           'System',
           'system@store.local',
           '$2a$10$7EqJtq98hPqEX7fNZaFWoOa1J2uQ7n0q8V4R8dP9yG5H6J7K8L9M0'
       );

ALTER TABLE products
    ADD COLUMN quantity INT NOT NULL DEFAULT 1;

ALTER TABLE products
    ADD COLUMN seller_id BIGINT NULL;

UPDATE products
SET seller_id = 1
WHERE seller_id IS NULL;

ALTER TABLE products
    MODIFY seller_id BIGINT NOT NULL;

ALTER TABLE products
    ADD CONSTRAINT products_seller_id_fk
        FOREIGN KEY (seller_id) REFERENCES users (id)
            ON DELETE NO ACTION;

CREATE INDEX products_seller_id_fk ON products (seller_id);

ALTER TABLE carts
    ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE carts
    ADD CONSTRAINT carts_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE;

CREATE INDEX carts_users_id_fk ON carts (user_id);
