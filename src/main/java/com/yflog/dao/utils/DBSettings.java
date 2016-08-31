package com.yflog.dao.utils;

import com.yflog.common.BaseSettings;

import java.io.IOException;

/**
 * Created by vincent on 8/31/16.
 */
public class DBSettings extends BaseSettings {

    public static final String JABC_MAX_ACTIVE = "jdbc.max.active";
    public static final int DEFAULT_JDBC_MAX_ACTIVE = 150;

    public static final String JDBC_MAX_IDLE = "jdbc.max.idle";
    public static final int DEFAULT_JDBC_MAX_IDLE = 50;

    public static final String JDBC_MAX_WAIT = "jdbc.max.wait";
    public static final int DEFAULT_JDBC_MAX_WAIT = 180;

    public static final String JDBC_USER = "jdbc.user";
    public static final String DEFAULT_JDBC_USER = "root";

    public static final String JDBC_PASS = "jdbc.pass";
    public static final String DEFAULT_JDBC_PASS = "";

    public static final String JDBC_URL = "jdbc.url";
    public static final String DEFAULT_JDBC_URL = "jdbc:mysql://localhost:3306?dumpQueriesOnException=true&amp;useUnicode=true&amp;characterEncoding=UTF-8";

    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String DEFAULT_JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static final String JDBC_VALIDATION_QUERY = "jdbc.validation.query";
    public static final String DEFAULT_JDBC_VALIDATION_QUERY = "/* ping */ SELECT 1";

    public static final String JDBC_REMOVE_ABANDONED = "jdbc.remove.abandoned";
    public static final boolean DEFAULT_JDBC_REMOVE_ABANDONED = true;

    public static final String JDBC_LOG_ABANDONED = "jdbc.log.abandoned";
    public static final boolean DEFAULT_JDBC_LOG_ABANDONED = true;

    // make it singleton

    public DBSettings(String path)  {
        super();
        try {
            initProps(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMaxActive() {
        return getSettingInt(JABC_MAX_ACTIVE, DEFAULT_JDBC_MAX_ACTIVE);
    }

    public int getMaxIdle() {
        return getSettingInt(JDBC_MAX_IDLE, DEFAULT_JDBC_MAX_IDLE);
    }

    public int getMaxWait() {
        return getSettingInt(JDBC_MAX_WAIT, DEFAULT_JDBC_MAX_WAIT);
    }

    public String getJdbcUser() {
        return getSetting(JDBC_USER, DEFAULT_JDBC_USER);
    }

    public String getJdbcPass() {
        return getSetting(JDBC_PASS, DEFAULT_JDBC_PASS);
    }

    public String getJdbcUrl() {
        return getSetting(JDBC_URL, DEFAULT_JDBC_URL);
    }

    public String getJdbcDriver() {
        return getSetting(JDBC_DRIVER, DEFAULT_JDBC_DRIVER);
    }

    public String getJdbcVerificationQuery() {
        return getSetting(JDBC_VALIDATION_QUERY, DEFAULT_JDBC_VALIDATION_QUERY);
    }

    public boolean getJdbcRemoveAbandoned() {
        return getSettingBoolean(JDBC_REMOVE_ABANDONED, DEFAULT_JDBC_REMOVE_ABANDONED);
    }

    public boolean getJdbcLogAbandoned() {
        return getSettingBoolean(JDBC_LOG_ABANDONED, DEFAULT_JDBC_LOG_ABANDONED);
    }

}
