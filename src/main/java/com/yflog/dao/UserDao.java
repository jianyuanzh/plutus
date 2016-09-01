package com.yflog.dao;

import com.yflog.dao.utils.DbUtils;
import com.yflog.dao.utils.QueryResource;
import com.yflog.domain.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vincent on 9/1/16.
 * User Dao
 */
public class UserDao extends AbstractDao {

    private final String dbName;
    private final String tbName;
    private final String sqlSelect;

    public UserDao() {
        dbName = "plutus";
        tbName = dbName + "users";
        sqlSelect = "SELECT users.id," +
                        "users.username," +
                        "users.email," +
                        "users.password" +
                        "FROM plutus.users";
    }

    protected User loadRecord(QueryResource qrs) throws SQLException {
        User user = new User();
        user.setId(DbUtils.getInt(qrs, "id"));
        user.setUsername(DbUtils.getString(qrs, "username"));
        user.setEmail(DbUtils.getString(qrs, "email"));
        user.setPassword(DbUtils.getString(qrs, "password"));

        return user;
    }




    public User addUser(Connection conn, User user) throws SQLException {
        update(conn,
                "insert into plutus.users (username, email, password) values(?,?,?)",
                user.getUsername(), user.getEmail(), user.getPassword());
        user.setId(getLastInsertId(conn));
        return user;
    }

    public void delete(Connection conn, User user) throws SQLException {
        String sql = "DELETE FROM " + tbName + " where id=?";
        update(conn, sql, user.getId());
    }

    public void update(Connection conn, User user) throws SQLException {
        String sql = "UPDATE " + tbName +
                " SET username=?, email=?, password=? WHERE id=?";
        update(conn, sql, user.getUsername(), user.getEmail(), user.getPassword());
    }

    public User getById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM " + tbName + " WHERE id=?";
        return (User) get(conn, sql, id);
    }

    public User getByUsername(Connection conn, String username) throws SQLException {
        String sql = "SELECT * FROM " + tbName + " WHERE username=?";
        return (User) get(conn, sql, username);
    }

    public User getByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT * FROM " + tbName + " WHERE email=?";
        return (User) get(conn, sql, email);
    }
}
