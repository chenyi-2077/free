package com.freelite.servlet;

import com.freelite.dao.BidDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BidListServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        try {
            int projectId = Integer.parseInt(pathInfo.replace("/", ""));
            req.setAttribute("bids", bidDao.findByProjectId(projectId));
            req.setAttribute("projectId", projectId);
            req.getRequestDispatcher("/C-bid/bidsOnProject.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/projects");
        }
    }
}
