package com.freelite.util;

import com.freelite.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限控制工具类
 * 提供登录检查、角色检查的静态方法
 */
public class AuthUtil {

    /**
     * 检查用户是否已登录
     * @return 当前登录用户，未登录则返回 null 并重定向到登录页
     */
    public static User requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            String redirect = req.getRequestURI().substring(req.getContextPath().length());
            if (req.getQueryString() != null) {
                redirect += "?" + req.getQueryString();
            }
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + redirect);
            return null;
        }
        return user;
    }

    /**
     * 检查用户是否已登录且拥有指定角色
     * @param role 期望角色，如 "employer" 或 "freelancer"
     * @return 当前登录用户，不符合条件则重定向并返回 null
     */
    public static User requireRole(HttpServletRequest req, HttpServletResponse resp, String role) throws IOException {
        User user = requireLogin(req, resp);
        if (user == null) return null;
        if (!user.getRole().equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return null;
        }
        return user;
    }

    /**
     * 检查用户是否已登录且拥有指定角色之一
     * @param roles 允许的角色数组
     */
    public static User requireAnyRole(HttpServletRequest req, HttpServletResponse resp, String... roles) throws IOException {
        User user = requireLogin(req, resp);
        if (user == null) return null;
        for (String role : roles) {
            if (user.getRole().equals(role)) {
                return user;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/projects");
        return null;
    }
}
