package com.freelite.servlet;

import com.freelite.dao.UserDAO;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注册 Servlet
 * A负责
 * 
 * GET  /register  → 显示注册页面
 * POST /register  → 处理注册请求
 */
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. 获取表单数据
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");         // "employer" or "freelancer"
        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");

        // 2. 简单校验
        if (email == null || password == null || role == null ||
            email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "邮箱和密码不能为空");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
            return;
        }

        // 3. 检查邮箱是否已注册
        User existing = userDAO.findByEmail(email.trim());
        if (existing != null) {
            req.setAttribute("error", "该邮箱已被注册");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
            return;
        }

        // 4. 创建用户
        User user = new User();
        user.setEmail(email.trim());
        user.setPassword(password); // 课设不做加密，实际项目要用 bcrypt
        user.setRole(role);
        user.setDisplayName(displayName != null ? displayName.trim() : "");
        user.setSkills(skills != null ? skills.trim() : "");

        int userId = userDAO.register(user);
        if (userId > 0) {
            // 注册成功 → 跳转到登录页
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } else {
            req.setAttribute("error", "注册失败，请稍后重试");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
        }
    }
}
