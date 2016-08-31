package com.yflog.dao.utils;

import org.apache.commons.dbcp.BasicDataSource;


/**
 * Created by vincent on 8/31/16.
 * Utils for get DB connection and run db operations
 */
public class DbUtils {

    private static final int DEFAULT_MAX_ACTIVE_COUNT= 150;
    private static final int DEFAULT_MAX_IDLE_COUNT = 50;
    private static final int DEFAULT_MAX_WAIT = 180;
    private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DEFAULT_SERVER = "localhost:3306";


    public static void initConnectionPool(DBSettings settings) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setMaxActive(settings.getMaxActive());
    }
}
