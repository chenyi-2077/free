package com.freelite.filter;

import com.freelite.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录认证过滤器
 * 自动拦截需要登录才能访问的路径，未登录用户跳转到登录页
 */
public class AuthFilter implements Filter {

    // 不需要登录即可访问的路径
    private static final Set<String> PUBLIC_PATHS = new HashSet<>(Arrays.asList(
            "/login", "/register", "/projects", "/project/"
    ));

    // 静态资源后缀
    private static final Set<String> STATIC_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".ico", ".svg", ".woff", ".woff2"
    ));

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 放行静态资源
        for (String ext : STATIC_EXTENSIONS) {
            if (path.toLowerCase().endsWith(ext)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 放行公开路径
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath + "/")) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 首页也放行
        if (path.equals("/") || path.equals("/index.jsp") || path.equals("/index.html")) {
            chain.doFilter(request, response);
            return;
        }

        // 检查登录
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            // 保存当前路径，登录后跳回
            String redirect = path;
            if (req.getQueryString() != null) {
                redirect += "?" + req.getQueryString();
            }
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + redirect);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing
    }
}
