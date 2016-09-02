package com.yflog.dao;

import com.yflog.dao.utils.DbUtils;
import com.yflog.dao.utils.QueryResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vincent on 9/1/16.
 */
public abstract class AbstractDao {
    public static final String PLUTUS_DB = "plutus";

    public AbstractDao() {
    }

    public int getLastInsertId(Connection conn) throws SQLException {
        return this.getInt(conn, "SELECT LAST_INSERT_ID()");
    }

    // --------- PROTECTED METHODS -----------
    protected abstract Object loadRecord(QueryResource qrs) throws SQLException;

    protected Object get(Connection conn, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            qrs = DbUtils.executeQuery(conn, sql, params);
            if (DbUtils.next(qrs)) {
                return loadRecord(qrs);
            }
            return null;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    public int update(Connection connection, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            qrs = DbUtils.executeUpdate(connection, sql, params);
            return qrs.rt;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    protected List<Object> getArray(Connection conn, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            List<Object> arr = new ArrayList<Object>();
            qrs = DbUtils.executeQuery(conn, sql, params);
            while (DbUtils.next(qrs)) {
                arr.add(loadRecord(qrs));
            }
            return arr;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    public List<String> getArrayOfString(Connection conne, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            List<String> strArr = new ArrayList<String>();
            qrs = DbUtils.executeQuery(conne, sql, params);
            while (DbUtils.next(qrs)) {
                strArr.add(DbUtils.getString(qrs, 1));
            }
            return strArr;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }


    protected int getInt(Connection conn, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            qrs = DbUtils.executeQuery(conn, sql, params);
            if (DbUtils.next(qrs)) {
                return DbUtils.getInt(qrs, 1);
            }
            return 0;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    protected String getString(Connection conn, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            qrs = DbUtils.executeQuery(conn, sql, params);
            if (DbUtils.next(qrs)) {
                return DbUtils.getString(qrs, 1);
            }
            return null;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    protected String createInBoundParams(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (sb.length() > 0) {
                sb.append(",?");
            }
            else {
                sb.append("?");
            }
        }

        return sb.toString();
    }

    protected int[] getInts(Connection conn, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            int[] r = null;
            qrs = DbUtils.executeQuery(conn, sql, params);
            if (DbUtils.next(qrs)) {
                int cnt = DbUtils.getColumnCount(qrs);
                r = new int[cnt];
                for (int i = 0; i < cnt; i++) {
                    r[i] = DbUtils.getInt(qrs, i + 1);
                }
            }
            return r;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    protected Map<Integer, Integer> getIntToIntMap(Connection conn, String sql, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            qrs = DbUtils.executeQuery(conn, sql, params);
            while (DbUtils.next(qrs)) {
                map.put(DbUtils.getInt(qrs, 1), DbUtils.getInt(qrs, 2));
            }
            return map;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }

    protected Map<Integer, int[]> getIntToIntArrayMap(Connection conn, String sql, int size, Object ... params) throws SQLException {
        QueryResource qrs = null;
        try {
            Map<Integer, int[]> map = new HashMap<Integer, int[]>();
            qrs = DbUtils.executeQuery(conn, sql, params);
            while (DbUtils.next(qrs)) {
                int[] array = new int[size];
                for (int i = 0; i < size; i++) {
                    array[i] = DbUtils.getInt(qrs, 2 + i);
                }
                map.put(DbUtils.getInt(qrs, 1), array);
            }

            return map;
        }
        finally {
            DbUtils.closeQuietly(qrs);
        }
    }
}
