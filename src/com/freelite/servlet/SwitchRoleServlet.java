package com.freelite.servlet;

import com.freelite.dao.UserDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SwitchRoleServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String newRole = req.getParameter("role");
        if (newRole == null || (!"employer".equals(newRole) && !"freelancer".equals(newRole))) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // 更新数据库
        userDao.updateRole(loginUser.getId(), newRole);

        // 更新 session 中的用户信息
        loginUser.setRole(newRole);
        req.getSession().setAttribute("user", loginUser);

        // 跳回个人主页
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
