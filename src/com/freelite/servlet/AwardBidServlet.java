package com.freelite.servlet;

import com.freelite.dao.BidDAO;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 选标 Servlet
 * C负责
 * 
 * POST /bid/award → 雇主选中某个竞标
 * 参数：bidId, projectId
 */
public class AwardBidServlet extends HttpServlet {

    private BidDAO bidDAO = new BidDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int bidId = Integer.parseInt(req.getParameter("bidId"));
        int projectId = Integer.parseInt(req.getParameter("projectId"));

        boolean success = bidDAO.awardBid(bidId, projectId, user.getId());
        resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
    }
}
