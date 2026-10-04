-- Körs en gång som root: skapar databasen och en användare för applikationen.
--   mysql -u root -p < database/create_database.sql
CREATE DATABASE IF NOT EXISTS webshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'webshop'@'localhost' IDENTIFIED BY 'webshop';
GRANT SELECT, INSERT, UPDATE, DELETE ON webshop.* TO 'webshop'@'localhost';
FLUSH PRIVILEGES;
