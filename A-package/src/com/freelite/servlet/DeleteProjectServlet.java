package com.freelite.servlet;

import com.freelite.dao.ProjectDao;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteProjectServlet extends HttpServlet {

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
        Project project = projectDao.findById(projectId);

        if (project == null || project.getEmployerId() != loginUser.getId()) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
            return;
        }

        // 只允许删除开放中的项目
        if (!"open".equals(project.getStatus())) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
            return;
        }

        projectDao.delete(projectId);
        resp.sendRedirect(req.getContextPath() + "/my/projects");
    }
}
