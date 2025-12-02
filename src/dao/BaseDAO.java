package dao;

import db.DBConnection;
import java.sql.*;

public abstract class BaseDAO {

    // Get database connection
    protected Connection getConnection() throws Exception {
        return DBConnection.getConnection();
    }

    // insert method that returns generated ID
    protected int insert(String sql, Object... params) throws Exception {
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // Generic update/delete method
    protected int update(String sql, Object... params) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();
        }
    }

    // Generic select method (optional helper)
    protected ResultSet select(String sql, Object... params) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }
}
