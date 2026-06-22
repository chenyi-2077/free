package com.freelite.servlet;

import com.freelite.dao.BidDAO;
import com.freelite.dao.ProjectDAO;
import com.freelite.model.Bid;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 项目详情 Servlet
 * B负责
 * 
 * GET /project/123 → 项目详情 + 已有竞标列表
 */
public class ProjectDetailServlet extends HttpServlet {

    private ProjectDAO projectDAO = new ProjectDAO();
    private BidDAO bidDAO = new BidDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 从路径取 ID：/project/123
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId;
        try {
            projectId = Integer.parseInt(pathInfo.replace("/", ""));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        // 查项目
        Project project = projectDAO.findById(projectId);
        if (project == null) {
            resp.sendError(404, "项目不存在");
            return;
        }

        // 查该项目的竞标列表
        List<Bid> bids = bidDAO.findByProject(projectId);

        req.setAttribute("project", project);
        req.setAttribute("bids", bids);
        req.setAttribute("isOwner", project.getEmployerId() == user.getId());
        req.getRequestDispatcher("/B-project/projectDetail.jsp").forward(req, resp);
    }
}
