package com.freelite.servlet;

import com.freelite.dao.BidDAO;
import com.freelite.model.Bid;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 某项目的竞标列表 Servlet（雇主查看）
 * C负责
 * 
 * GET /bids/123 → 查看项目123的所有竞标
 */
public class BidListServlet extends HttpServlet {

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

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(pathInfo.replace("/", ""));
        List<Bid> bids = bidDAO.findByProject(projectId);

        req.setAttribute("bids", bids);
        req.setAttribute("projectId", projectId);
        req.getRequestDispatcher("/C-bid/bidsOnProject.jsp").forward(req, resp);
    }
}
