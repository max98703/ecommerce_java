package dao;

import java.sql.ResultSet;
import models.Buyer;

public class BuyerDAO extends BaseDAO {

    // Adds a new buyer if the email doesn't exist; returns existing buyer ID otherwise
    public int addBuyer(Buyer b) throws Exception {
        if (isEmailExists(b.email)) {
            String sql = "SELECT id FROM buyers WHERE email = ?";
            try (ResultSet rs = runQuery(sql, b.email)) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        // Insert new buyer
        String insertSql = "INSERT INTO buyers (name, email) VALUES (?, ?)";
        return executeInsert(insertSql, b.name, b.email);
    }

    // Checks if a buyer with the given email already exists
    public boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT * FROM buyers WHERE email = ?";
        try (ResultSet rs = runQuery(sql, email)) {
            return rs.next();
        }
    }
    
    // Update buyer balance by adding the specified amount
    public int addBalance(int buyerId, double amount) throws Exception {
        String sql = "UPDATE buyers SET balance = balance + ? WHERE id = ?";
        return executeUpdate(sql, amount, buyerId);
    }
    
     // Fetch buyer balance
    public double fetchBalance(int id) throws Exception {
        String sql = "SELECT balance FROM buyers WHERE id = ?";
        try (ResultSet rs = runQuery(sql, id)) {
            if (rs.next()) return rs.getInt("balance");
        }
        return 0.0;
    }

    public double updateBalance(int id, double amount) throws Exception {
        String sql = "UPDATE buyers SET balance = balance - ? WHERE id = ?";
        return executeUpdate(sql, amount, id);
    }
}
