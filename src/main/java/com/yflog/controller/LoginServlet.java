package com.yflog.controller;

import com.yflog.service.UserService;
import com.yflog.service.exception.AuthException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by rooter on 2016/8/31.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/pages/login.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doLogin(req, resp);
    }

    private void doLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            UserService.validUser(username, password);
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("login", true);

            resp.sendRedirect("/index.jsp");
        } catch (AuthException e) {
            resp.getWriter().write(String.format("Failed for Login - %d:%s", e.getErrCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Failed for Login - " + e.getMessage());
        }

    }
}
