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
    public static final String DEFAULT_JDBC_URL = "";

    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String DEFAULT_JDBC_DRIVER = "com.mysql.jdbc.Driver";

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
}
