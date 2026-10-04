-- Testdata.
--   mysql -u root -p webshop < database/data.sql
-- Lösenorden är: admin / lager / kund

INSERT INTO users (username, password, role) VALUES
('admin', 'admin', 'ADMIN'),
('lager', 'lager', 'WAREHOUSE'),
('kund',  'kund', 'CUSTOMER');

INSERT INTO items (name, description, category, price, stock) VALUES
('Kaffebryggare', 'Bryggare för 10 koppar', 'Kök', 499, 5),
('Brödrost', 'Rostar två skivor', 'Kök', 249, 0),
('Vattenkokare', '1,7 liter', 'Kök', 299, 12),
('Hörlurar', 'Trådlösa hörlurar', 'Elektronik', 899, 3),
('USB-kabel', 'USB-C, 1 meter', 'Elektronik', 79, 50),
('Skrivbordslampa', 'LED-lampa', 'Hem', 349, 7);
