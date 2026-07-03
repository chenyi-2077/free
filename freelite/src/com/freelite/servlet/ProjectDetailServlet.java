package com.freelite.servlet;

import com.freelite.dao.BidDao;
import com.freelite.dao.ProjectDao;
import com.freelite.model.Bid;
import com.freelite.model.Project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/project/detail")
public class ProjectDetailServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int id = Integer.parseInt(idStr);
        Project project = projectDao.findById(id);
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        List<Bid> bids = bidDao.findByProjectId(id);
        req.setAttribute("project", project);
        req.setAttribute("bids", bids);
        req.getRequestDispatcher("/B-project/projectDetail.jsp").forward(req, resp);
    }
}
