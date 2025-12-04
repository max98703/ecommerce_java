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
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Failed to obtain DB connection", ex);
            throw ex;
        }
    }

    /**
     * Insert operation that returns generated ID if available.
     */
    protected int executeInsert(String query, Object... values) throws SQLException {
        int generatedId = -1;

        try (Connection con = open();
             PreparedStatement st = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            bindParams(st, values);
            st.executeUpdate();

            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Insert failed: " + query, ex);
            throw ex;
        }
        return generatedId;
    }

    /**
     * Update / delete helper.
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection con = open();
            PreparedStatement st = con.prepareStatement(sql)) {

            bindParams(st, params);
            return st.executeUpdate();

        } catch (SQLException ex) {
            log.log(Level.INFO, "DB update failed", ex);
            throw ex;
        }
    }

    /**
     * select helper.
     */
    protected QueryHandle runQuery(String sql, Object... args) throws SQLException {
        Connection c = open(); // not closed here
        PreparedStatement ps = c.prepareStatement(sql);
        bindParams(ps, args);

        ResultSet rs = ps.executeQuery();
        return new QueryHandle(c, ps, rs); // wrapper ensures cleanup later
    }

    /**
     * A small helper method used internally to bind parameters.
     */
    private void bindParams(PreparedStatement st, Object... list) throws SQLException {
        for (int idx = 0; idx < list.length; idx++) {
            st.setObject(idx + 1, list[idx]);
        }
    }

    /**
     * A simple wrapper class to hold query resources for later cleanup.
     */
    public static class QueryHandle {
        public final Connection c;
        public final PreparedStatement ps;
        public final ResultSet rs;

        public QueryHandle(Connection c, PreparedStatement ps, ResultSet rs) {
            this.c = c;
            this.ps = ps;
            this.rs = rs;
        }

        public void close() {
            try { rs.close(); } catch (Exception ignore) {}
            try { ps.close(); } catch (Exception ignore) {}
            try { c.close(); } catch (Exception ignore) {}
        }
    }
}
