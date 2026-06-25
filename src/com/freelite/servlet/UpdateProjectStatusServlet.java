package com.freelite.servlet;

import com.freelite.dao.ProjectDao;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateProjectStatusServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int projectId = Integer.parseInt(req.getParameter("id"));
        String newStatus = req.getParameter("status");
        String redirect = req.getParameter("redirect");

        Project project = projectDao.findById(projectId);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        // 校验状态转换是否合法
        String current = project.getStatus();
        boolean valid = false;
        switch (newStatus) {
            case "cancelled":
                // open/in_progress 可以取消
                if ("open".equals(current) || "in_progress".equals(current)) valid = true;
                break;
            case "open":
                // 已取消的可重新开放
                if ("cancelled".equals(current)) valid = true;
                break;
        }

        if (valid) {
            projectDao.updateStatus(projectId, newStatus);
        }

        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + redirect);
        } else {
            resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
        }
    }
}
