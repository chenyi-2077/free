package com.freelite.servlet;

import com.freelite.dao.UserDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "请填写邮箱和密码");
            req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
            return;
        }

        User user = userDao.findByEmail(email.trim());
        if (user == null || !user.getPassword().equals(password.trim())) {
            req.setAttribute("error", "邮箱或密码错误");
            req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/projects");
    }
}
