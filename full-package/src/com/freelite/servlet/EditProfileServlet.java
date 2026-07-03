package com.freelite.servlet;

import com.freelite.dao.UserDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditProfileServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/A-user/editProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");

        loginUser.setDisplayName(displayName);
        loginUser.setSkills(skills);
        userDao.update(loginUser);

        // 刷新 session 中的用户
        User refreshed = userDao.findById(loginUser.getId());
        req.getSession().setAttribute("user", refreshed);

        req.setAttribute("success", "保存成功");
        req.getRequestDispatcher("/A-user/editProfile.jsp").forward(req, resp);
    }
}
