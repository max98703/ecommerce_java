package dao;

import java.sql.ResultSet;
import models.Buyer;

public class BuyerDAO extends BaseDAO {

    public int addBuyer(Buyer b) throws Exception {
        if (isEmailExists(b.email)) {
            String sql = "SELECT id FROM buyers WHERE email = ?";
            try (ResultSet rs = select(sql, b.email)) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        // Insert new buyer
        String insertSql = "INSERT INTO buyers (name, email) VALUES (?, ?)";
        return insert(insertSql, b.name, b.email);
    }

    public boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT 1 FROM buyers WHERE email = ?";
        try (ResultSet rs = select(sql, email)) {
            return rs.next();
        }
    }
}
