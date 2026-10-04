-- Tabellerna för webbshoppen.
--   mysql -u root -p webshop < database/schema.sql

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

-- role är CUSTOMER (kund), WAREHOUSE (lagerpersonal) eller ADMIN.
CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role     VARCHAR(20) NOT NULL,
    active   BOOLEAN     NOT NULL DEFAULT TRUE
);

-- price är i hela kronor. stock är antal i lager (varulagret).
CREATE TABLE items (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    category    VARCHAR(50),
    price       INT NOT NULL,
    stock       INT NOT NULL DEFAULT 0
);

-- status är 'Packas' (ny order) eller 'Skickad' (lagerpersonalen har skickat den).
CREATE TABLE orders (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT NOT NULL,
    created    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total      INT NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'Packas',
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- price sparas per rad så att ordern inte ändras om varans pris ändras senare.
CREATE TABLE order_items (
    order_id INT NOT NULL,
    item_id  INT NOT NULL,
    quantity INT NOT NULL,
    price    INT NOT NULL,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);
