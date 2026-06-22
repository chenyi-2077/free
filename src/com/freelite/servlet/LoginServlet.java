package com.freelite.servlet;

import com.freelite.dao.UserDAO;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录 Servlet
 * A负责
 * 
 * GET  /login  → 显示登录页面
 * POST /login  → 处理登录
 */
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 如果已经登录了，直接跳首页
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        // 提示注册成功
        if ("1".equals(req.getParameter("registered"))) {
            req.setAttribute("success", "注册成功，请登录");
        }

        req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // 校验
        if (email == null || password == null || email.trim().isEmpty()) {
            req.setAttribute("error", "请输入邮箱和密码");
            req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
            return;
        }

        // 查用户
        User user = userDAO.findByEmail(email.trim());
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("error", "邮箱或密码错误");
            req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
            return;
        }

        // 登录成功 → 存 Session
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/projects");
    }
}
