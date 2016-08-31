package com.yflog.dao.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.dbcp.BasicDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;


/**
 * Created by vincent on 8/31/16.
 * Utils for get DB connection and run db operations
 */
public class DbUtils {

    public static final String MYSQL_JNDI_REF = "java:/comp/env/jdbc/plutus";

    private static final int DEFAULT_MAX_ACTIVE_COUNT = 150;
    private static final int DEFAULT_MAX_IDLE_COUNT = 50;
    private static final int DEFAULT_MAX_WAIT = 180;
    private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DEFAULT_SERVER = "localhost:3306";

    private static DataSource dataSource = null;


    public static void initConnectionPool(DBSettings settings) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setMaxActive(settings.getMaxActive());
        dataSource.setMaxIdle(settings.getMaxIdle());
        dataSource.setMaxWait(settings.getMaxWait());

        dataSource.setUrl(settings.getJdbcUrl());
        dataSource.setUsername(settings.getJdbcUser());
        dataSource.setPassword(settings.getJdbcPass());
        dataSource.setDriverClassName(settings.getJdbcDriver());
        dataSource.setRemoveAbandoned(settings.getJdbcRemoveAbandoned());
        dataSource.setLogAbandoned(settings.getJdbcLogAbandoned());
        dataSource.setValidationQuery(settings.getJdbcVerificationQuery());

        DbUtils.dataSource = dataSource;
    }

    public static void initConnectionPoolByJndi() throws Exception {
        Context context = new InitialContext();

        dataSource = (DataSource) context.lookup(MYSQL_JNDI_REF);

        if (null == dataSource) {
            throw new Exception("Cannot get DataSource for MySql from " + MYSQL_JNDI_REF);
        }

        Connection conn = dataSource.getConnection();
        if (null == conn) {
            throw new Exception("Cannot get database connection");
        }

        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println(String.format("databaseUrl=%s, driver=%s, user=%s",
                metaData.getURL(), metaData.getDriverName(), metaData.getUserName()));
        conn.close();
    }

    public static Connection getConnection(boolean autiCommit) throws SQLException {
        Connection conn = getConnection();
        setAutoCommit(conn, autiCommit);
        return conn;
    }


    public static Connection getAutoCommitConnection() throws SQLException {

        Connection conn = getConnection();

        if (!conn.getAutoCommit()) {
            conn.setAutoCommit(true);
        }

        return conn;
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                setAutoCommit(conn, false);
            } catch (SQLException ignored) {
            }

            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(QueryResource qrs) {
        if (qrs != null) {
            DbUtils.closeQuietly(qrs.pstmt);
            DbUtils.closeQuietly(qrs.rs);

            qrs.pstmt = null;
            qrs.rs = null;
            qrs.rt = 0;
        }
    }

    public static void closeQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }

    /**
     * Set auto commit if needed. The old autocommit setting will be returned to caller,
     * so caller may want to set back after operations.
     *
     * @throws SQLException during get current auto commit setting.
     */
    public static boolean setAutoCommit(Connection conn, boolean autoCommit) throws SQLException {
        boolean oldCommit = conn.getAutoCommit();
        if (oldCommit != autoCommit) {
            conn.setAutoCommit(autoCommit);
        }

        return oldCommit;
    }

    public static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    // helpers for accessing
    public static boolean dbExists(Connection conn, String dbName) {
        Preconditions.checkNotNull(conn, "Connection should not be null");
        int count = 0;
        try {
            String sql = "select count(*) from information_schema.SCHEMATA where SCHEMA_NAME = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dbName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ignored) {
        }

        return count > 0;
    }

    public static QueryResource executeQuery(Connection conn, String sql, Object... params) throws SQLException {

        Preconditions.checkNotNull(conn, "Connection should not be null");
        QueryResource qrs = new QueryResource();
        try {
            qrs.pstmt = conn.prepareStatement(sql);
            setParams(qrs.pstmt, params);
            qrs.rs = qrs.pstmt.executeQuery();
            return qrs;
        } catch (SQLException e) {
            // TODO log
            throw e;
        }
    }

    public static QueryResource executeUpdate(Connection conn, String sql, Object... params) throws SQLException {
        Preconditions.checkNotNull(conn, "Connection should not be null");
        try {
            QueryResource qrs = new QueryResource();
            qrs.pstmt = conn.prepareStatement(sql);
            setParams(qrs.pstmt, params);
            qrs.rt = qrs.pstmt.executeUpdate();

            return qrs;
        } catch (SQLException e) {
            // TODO need log
            throw e;
        }
    }

    public static void executeQueryIgnoreResult(Connection conn, String sql, Object... params) throws SQLException {
        QueryResource qrs = null;
        try {
            qrs = executeQuery(conn, sql, params);
        } finally {
            closeQuietly(qrs);
        }
    }

    public static void executeUpdateIgnoreResult(Connection conn, String sql, Object... params) throws SQLException {
        QueryResource qrs = null;
        try {
            qrs = executeUpdate(conn, sql, params);
        } finally {
            closeQuietly(qrs);
        }
    }

    public static boolean next(QueryResource qrs) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.next();
    }

    public static int getInt(QueryResource qrs, String columnLabel) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getInt(columnLabel);
    }

    public static int getInt(QueryResource qrs, int columnIndex) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");
        return qrs.rs.getInt(columnIndex);
    }

    public static Object getObject(QueryResource qrs, String columnLabel) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getObject(columnLabel);
    }

    public static Object getObject(QueryResource qrs, int index) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getObject(index);
    }

    public static String getString(QueryResource qrs, String label) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getString(label);
    }

    public static String getString(QueryResource qrs, int index) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getString(index);
    }

    public static long getLong(QueryResource qrs, String label) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getLong(label);
    }

    public static long getLong(QueryResource qrs, int index) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getLong(index);
    }

    public static Timestamp getTimestamp(QueryResource qrs, String label) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getTimestamp(label);
    }

    public static Timestamp getTimestamp(QueryResource qrs, int index) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getTimestamp(index);
    }

    public static int getColumnCount(QueryResource qrs) throws SQLException {
        Preconditions.checkNotNull(qrs, "QueryResource should not be null");
        Preconditions.checkNotNull(qrs.rs, "ResultSet in QueryResource should not be null");

        return qrs.rs.getMetaData().getColumnCount();
    }
    // private helpers
    private static void setSingleParam(PreparedStatement pstmt, int index, Object param) throws SQLException {
        Preconditions.checkNotNull(pstmt, "PreparedStatement should not be null");
        if (param == null) {
            pstmt.setString(index, null);
        } else if (param instanceof Integer) {
            pstmt.setInt(index, (Integer) param);
        } else if (param instanceof Long) {
            pstmt.setLong(index, (Long) param);
        } else if (param instanceof byte[]) {
            pstmt.setBytes(index, (byte[]) param);
        } else {
            pstmt.setString(index, param.toString());
        }
    }

    private static void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        Preconditions.checkNotNull(pstmt, "PreparedStatement should not be null");
        int i = 0;
        for (Object o : params) {
            if (o == null) {
                setSingleParam(pstmt, i + 1, null);
                i++;
            } else if (o instanceof int[]) {
                for (int item : (int[]) o) {
                    setSingleParam(pstmt, i + 1, item);
                    i++;
                }
            } else if (o instanceof long[]) {
                for (long item : (long[]) o) {
                    setSingleParam(pstmt, i + 1, item);
                    i++;
                }
            } else if (o instanceof Object[]) {
                for (Object item : (Object[]) o) {
                    setSingleParam(pstmt, i, item);
                    i++;
                }
            } else if (o instanceof Collection) {
                for (Object item : (Collection) o) {
                    setSingleParam(pstmt, i, item);
                    i++;
                }
            } else {
                setSingleParam(pstmt, i + 1, o);
                i++;
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
