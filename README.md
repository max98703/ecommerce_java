# Java E-Commerce Project Setup Guide

## 1. Requirements

Before running the project, make sure the following are installed:

- **Java Development Kit (JDK) 17+**  
  [Download JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

- **XAMPP** (includes Apache + MySQL)  
  [Download XAMPP](https://www.apachefriends.org/download.html)

- **MySQL Connector/J (JDBC driver)**  
  Place the jar in the `lib/` folder of the project (e.g., `lib/mysql-connector-j-9.5.0.jar`)

- **IDE or Text Editor** (Optional)  
  e.g., IntelliJ IDEA, VS Code

- **Terminal / Command Line** for compiling and running Java code

---

## 2. Database Setup (XAMPP / MySQL)

1. Start **XAMPP** and ensure **MySQL** is running.
2. Open **phpMyAdmin** or MySQL CLI.
3. Create a new database, e.g., `ecommerce_db`.
4. Run the following SQL commands to create tables:

```sql
CREATE TABLE sellers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE buyers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT,
    name VARCHAR(100),
    price DOUBLE,
    quantity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES sellers(id)
);

CREATE TABLE basket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (buyer_id) REFERENCES buyers(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE purchases (
    id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    total_price DOUBLE NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES buyers(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

# Compile all Java files
javac -cp "lib/mysql-connector-j-9.5.0.jar" $(find src -name "*.java") -d out 2>/dev/null || javac -cp "lib/mysql-connector-j-9.5.0.jar" src\**\*.java -d out

# Run the Main class
java -cp "out:lib/mysql-connector-j-9.5.0.jar" Main 2>nul || java -cp "out;lib/mysql-connector-j-9.5.0.jar" Main
