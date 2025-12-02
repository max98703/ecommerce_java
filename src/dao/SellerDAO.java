package dao;

import java.sql.ResultSet;
import models.Seller;

public class SellerDAO extends BaseDAO {

    // Add seller
    public int addSeller(Seller s) throws Exception {
        // Optional: check if email already exists
        String checkSql = "SELECT id FROM sellers WHERE email = ?";
        try (ResultSet rs = select(checkSql, s.email)) {
            if (rs.next()) return rs.getInt("id"); // Return existing seller ID
        }

        String insertSql = "INSERT INTO sellers (name, email) VALUES (?, ?)";
        return insert(insertSql, s.name, s.email);
    }
}
