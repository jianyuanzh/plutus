package com.yflog.controller;

import com.yflog.dao.utils.DbUtils;
import com.yflog.dao.utils.QueryResource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Created by Vincent on 2016/8/31.
 * Servlet for registering
 */
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = DbUtils.getAutoCommitConnection();
            if (DbUtils.dbExists(conn, "plutus")) {
                System.out.println("plutus db exists");
            }
            else {
                System.err.println("plutus db not exist");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(conn);
        }
        req.getRequestDispatcher("/pages/register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
