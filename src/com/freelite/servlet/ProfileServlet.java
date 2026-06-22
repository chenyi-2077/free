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
 * 个人主页 Servlet
 * A负责
 * 
 * GET  /profile      → 自己的个人主页
 * GET  /profile/123  → 查看用户123的主页
 * POST /profile      → 更新个人资料
 */
public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User loginUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 解析路径 /profile 或 /profile/123
        String pathInfo = req.getPathInfo();
        int profileUserId;

        if (pathInfo == null || pathInfo.equals("/")) {
            // 查看自己的主页
            profileUserId = loginUser.getId();
        } else {
            // 查看别人的主页
            try {
                profileUserId = Integer.parseInt(pathInfo.replace("/", ""));
            } catch (NumberFormatException e) {
                profileUserId = loginUser.getId();
            }
        }

        User profileUser = userDAO.findById(profileUserId);
        if (profileUser == null) {
            resp.sendError(404, "用户不存在");
            return;
        }

        req.setAttribute("profileUser", profileUser);
        req.setAttribute("isOwnProfile", profileUserId == loginUser.getId());
        req.getRequestDispatcher("/A-user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User loginUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 更新资料
        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");

        loginUser.setDisplayName(displayName != null ? displayName.trim() : "");
        loginUser.setSkills(skills != null ? skills.trim() : "");

        if (userDAO.updateProfile(loginUser)) {
            // 更新 Session 中的用户信息
            session.setAttribute("user", loginUser);
            req.setAttribute("success", "更新成功");
        } else {
            req.setAttribute("error", "更新失败");
        }

        req.setAttribute("profileUser", loginUser);
        req.setAttribute("isOwnProfile", true);
        req.getRequestDispatcher("/A-user/profile.jsp").forward(req, resp);
    }
}
