package com.freelite.servlet;

import com.freelite.dao.BidDao;
import com.freelite.dao.ProjectDao;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.freelite.util.AuthUtil;

public class ProjectDetailServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = AuthUtil.requireLogin(req, resp);
        if (loginUser == null) return;
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        try {
            int projectId = Integer.parseInt(pathInfo.replace("/", ""));
            Project project = projectDao.findById(projectId);
            if (project == null) {
                resp.sendRedirect(req.getContextPath() + "/projects");
                return;
            }

            req.setAttribute("project", project);
            req.setAttribute("bids", bidDao.findByProjectId(projectId));
            req.setAttribute("isOwner", loginUser.getId() == project.getEmployerId());
            req.getRequestDispatcher("/B-project/projectDetail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/projects");
        }
    }
}
