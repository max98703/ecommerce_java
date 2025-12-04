package dao;

import java.sql.ResultSet;
import models.Seller;

public class SellerDAO extends BaseDAO {

    // Adds a new seller if the email doesn't exist; returns existing seller ID otherwise
    public int addSeller(Seller s) throws Exception {
        
        // Check if seller with the email already exists
        String checkSql = "SELECT id FROM sellers WHERE email = ?";
        try (ResultSet rs = runQuery(checkSql, s.email)) {
            if (rs.next()) return rs.getInt("id"); // Return existing seller ID
        }

        String insertSql = "INSERT INTO sellers (name, email) VALUES (?, ?)";
        return executeInsert(insertSql, s.name, s.email);
    }
    
    // Update seller balance by adding the specified amount
    public int updateSellerBalance(int sellerId, double amount) throws Exception {
        String sql = "UPDATE sellers SET balance = balance + ? WHERE id = ?";
        return executeUpdate(sql, amount, sellerId);
    }

}
