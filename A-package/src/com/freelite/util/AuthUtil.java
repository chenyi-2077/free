package com.freelite.util;

import com.freelite.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限控制工具类
 * 提供登录检查、角色检查的静态方法，后续可替换各 Servlet 中重复的校验代码
 */
public class AuthUtil {

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

    public static User requireRole(HttpServletRequest req, HttpServletResponse resp, String role) throws IOException {
        User user = requireLogin(req, resp);
        if (user == null) return null;
        if (!user.getRole().equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return null;
        }
        return user;
    }

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
