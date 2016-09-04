package com.yflog.service;

import com.yflog.common.ErrorCodes;
import com.yflog.dao.UserDao;
import com.yflog.dao.utils.DbUtils;
import com.yflog.domain.User;
import com.yflog.service.exception.AuthException;
import com.yflog.service.exception.ParamException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.yflog.common.ErrorCodes.AUTH_PASSWORD_MISMATCH;
import static com.yflog.common.ErrorCodes.AUTH_USER_NOT_REGISTERED;

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

    public static void validUser(String username, String password) throws AuthException, SQLException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new AuthException(ErrorCodes.COMMON_INVALID_PARAMETERS, "Username and Password cannot be null");
        }

        Connection conn = null;
        try {
            conn = DbUtils.getAutoCommitConnection();
            User user = new UserDao().getByUsername(conn, username);

            if (user == null) {
                throw new AuthException(AUTH_USER_NOT_REGISTERED, String.format("User %s not registered", username));
            }

            if (!password.equals(user.getPassword())) {
                throw new AuthException(AUTH_PASSWORD_MISMATCH, "Password mismatch");
            }

        }
        finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
