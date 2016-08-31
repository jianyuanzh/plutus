package com.yflog.controller;

import com.yflog.dao.utils.DbUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by rooter on 2016/8/31.
 */
public class PlutusListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            DbUtils.initConnectionPoolByJndi();
        } catch (Exception e) {
            e.printStackTrace();

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
                System.out.println("DB Connection pool initial failed, redeploy plutus");
            }
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
