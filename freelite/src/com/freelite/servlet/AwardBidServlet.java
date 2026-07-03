package com.freelite.servlet;

import com.freelite.dao.*;
import com.freelite.model.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bid/award")
public class AwardBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();
    private ProjectDao projectDao = new ProjectDao();
    private OrderDao orderDao = new OrderDao();
    private WalletDao walletDao = new WalletDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String bidIdStr = req.getParameter("bidId");
        if (bidIdStr == null || bidIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int bidId = Integer.parseInt(bidIdStr);
        Bid bid = bidDao.findById(bidId);
        if (bid == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        Project project = projectDao.findById(bid.getProjectId());
        if (project == null || project.getEmployerId() != user.getId()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        // Reject all other bids
        java.util.List<Bid> allBids = bidDao.findByProjectId(bid.getProjectId());
        for (Bid b : allBids) {
            if (b.getId() == bidId) {
                bidDao.updateStatus(b.getId(), "accepted");
            } else {
                bidDao.updateStatus(b.getId(), "rejected");
            }
        }

        // Update project status
        projectDao.updateStatus(project.getId(), "in_progress");

        // Freeze employer's wallet
        walletDao.freeze(user.getId(), bid.getAmount());

        // Create order
        Order order = new Order();
        order.setProjectId(project.getId());
        order.setEmployerId(project.getEmployerId());
        order.setFreelancerId(bid.getFreelancerId());
        order.setAmount(bid.getAmount());
        orderDao.insert(order);

        resp.sendRedirect(req.getContextPath() + "/project/detail?id=" + project.getId());
    }
}
