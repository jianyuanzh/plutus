package com.yflog.controller;


import com.yflog.domain.User;
import com.yflog.service.UserService;
import com.yflog.service.exception.ParamException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;

/**
 * Created by Vincent on 2016/8/31.
 * Servlet for registering
 */
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/pages/register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        try {
            User added = UserService.addUser(user);
            resp.getWriter().write("User " + user.getUsername() + " is created, use it to login now");
        }
        catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("User creation failed - " + e.getMessage());
        }

    }

    protected InputStream getRequestInputStream(HttpServletRequest request) throws IOException {
        InputStream rin = request.getInputStream();

        String contentEncoding = request.getHeader("Content-Encoding");
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            rin = new GZIPInputStream(rin);
        }

        return rin;
    }
}
