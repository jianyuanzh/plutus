package com.yflog.service;

import com.yflog.dao.UserDao;
import com.yflog.dao.utils.DbUtils;
import com.yflog.domain.User;
import com.yflog.service.exception.ParamException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vincent on 9/1/16.
 * User service
 */
public class UserService {
    public static User addUser(User user) throws ParamException, SQLException {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ParamException("user name should not be empty");
        }

        Connection conn = null;
        try {
            conn = DbUtils.getAutoCommitConnection();
            User returnVal = new UserDao().addUser(conn, user);
            return returnVal;
        }
        catch (SQLException e) {
            DbUtils.rollbackQuietly(conn);
            throw e;
        }
        finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
