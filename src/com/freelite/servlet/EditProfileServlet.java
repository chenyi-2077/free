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
 * 编辑资料 Servlet（独立页面版）
 * A负责
 * 
 * GET  /profile/edit → 编辑资料页面
 * POST /profile/edit → 保存修改
 */
public class EditProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/A-user/editProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");

        user.setDisplayName(displayName != null ? displayName.trim() : "");
        user.setSkills(skills != null ? skills.trim() : "");

        if (userDAO.updateProfile(user)) {
            session.setAttribute("user", user);
            req.setAttribute("success", "✅ 保存成功");
        } else {
            req.setAttribute("error", "❌ 保存失败，请重试");
        }

        req.getRequestDispatcher("/A-user/editProfile.jsp").forward(req, resp);
    }
}
