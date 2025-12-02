install java install xampp

CREATE TABLE sellers ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100) );

CREATE TABLE buyers ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100) );

CREATE TABLE items ( id INT AUTO_INCREMENT PRIMARY KEY, seller_id INT, name VARCHAR(100), price DOUBLE, quantity INT, FOREIGN KEY (seller_id) REFERENCES sellers(id) );

-- Basket table (items added by buyer before purchase) CREATE TABLE basket ( id INT AUTO_INCREMENT PRIMARY KEY, buyer_id INT NOT NULL, item_id INT NOT NULL, quantity INT NOT NULL DEFAULT 1, FOREIGN KEY (buyer_id) REFERENCES buyers(id), FOREIGN KEY (item_id) REFERENCES items(id) );

-- Purchases table (records completed purchases) CREATE TABLE purchases ( id INT AUTO_INCREMENT PRIMARY KEY, buyer_id INT NOT NULL, item_id INT NOT NULL, quantity INT NOT NULL, total_price DOUBLE NOT NULL, purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (buyer_id) REFERENCES buyers(id), FOREIGN KEY (item_id) REFERENCES items(id) );

javac -cp "lib/mysql-connector-j-9.5.0.jar" $(find src -name "*.java") -d out java -cp "out:lib/mysql-connector-j-9.5.0.jar" Main # Mac/Linux java -cp "out;lib/mysql-connector-j-9.5.0.jar" Main # Windows
