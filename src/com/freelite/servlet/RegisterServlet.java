package com.freelite.servlet;

import com.freelite.dao.UserDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");

        // 检查邮箱是否已注册
        if (userDao.findByEmail(email) != null) {
            req.setAttribute("error", "该邮箱已被注册");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
            return;
        }

        User user = new User(email, password, role, displayName, skills);
        int id = userDao.insert(user);
        if (id > 0) {
            // 注册成功，自动登录
            user.setId(id);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/projects");
        } else {
            req.setAttribute("error", "注册失败，请重试");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
        }
    }
}
