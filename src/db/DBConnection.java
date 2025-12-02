package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/ecommerce_java?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Alina123@";

    public static Connection getConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get the connection
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            
            if (conn == null) {
                System.out.println("DB Connection returned null!");
            }
            
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found!");
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}
