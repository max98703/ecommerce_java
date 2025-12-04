package dao;

import java.sql.ResultSet;
import models.Buyer;

public class BuyerDAO extends BaseDAO {

    // Adds a new buyer if the email doesn't exist; returns existing buyer ID otherwise
    public int addBuyer(Buyer b) throws Exception {
        if (isEmailExists(b.email)) {
            String sql = "SELECT id FROM buyers WHERE email = ?";
            try (ResultSet rs = runQuery(sql, b.email).rs) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        // Insert new buyer
        String insertSql = "INSERT INTO buyers (name, email) VALUES (?, ?)";
        return executeInsert(insertSql, b.name, b.email);
    }

    // Checks if a buyer with the given email already exists
    public boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT 1 FROM buyers WHERE email = ?";
        try (ResultSet rs = runQuery(sql, email).rs) {
            return rs.next();
        }
    }
}
