package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base DAO class providing common database operations.
 */
public abstract class BaseDAO {

    private static final Logger log = Logger.getLogger(BaseDAO.class.getName());
    
    //check for connection before each operation
    protected Connection open() throws SQLException {
        //wrap connection getting with logging
        try {
            return DBConnection.getConnection();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to obtain DB connection", e);
            throw e;
        }
    }

    /**
     * Insert operation that returns generated ID if available.
     */
    protected int executeInsert(String query, Object... values) throws SQLException {

        try (Connection con = open();
            PreparedStatement st = con.prepareStatement(query)) {

            bindParams(st, values);
            return st.executeUpdate();

        } catch (SQLException e) {
            log.log(Level.WARNING, "Insert failed: ", e);
            throw e;
        }
    }

    /**
     * Update / delete helper.
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection con = open();
            PreparedStatement st = con.prepareStatement(sql)) {

            bindParams(st, params);
            return st.executeUpdate();

        } catch (SQLException e) {
            log.log(Level.INFO, "DB update failed", e);
            throw e;
        }
    }

    /**
     * select helper.
     */
    protected ResultSet runQuery(String sql, Object... args) throws SQLException {
        Connection c = open();
        PreparedStatement ps = c.prepareStatement(sql);
        bindParams(ps, args);

        ResultSet rs = ps.executeQuery();
        return rs;
    }

    /**
     * Binds parameters to a PreparedStatement.
     */
    private void bindParams(PreparedStatement st, Object... list) throws SQLException {
        for (int idx = 0; idx < list.length; idx++) {
            st.setObject(idx + 1, list[idx]);
        }
    }

}
