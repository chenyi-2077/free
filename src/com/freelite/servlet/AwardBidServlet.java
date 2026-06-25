package com.freelite.servlet;

import com.freelite.dao.*;
import com.freelite.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.freelite.util.AuthUtil;

public class AwardBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();
    private ProjectDao projectDao = new ProjectDao();
    private OrderDao orderDao = new OrderDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = AuthUtil.requireLogin(req, resp);
        if (loginUser == null) return;

        String bidIdStr = req.getParameter("bidId");
        String projectIdStr = req.getParameter("projectId");

        int bidId = Integer.parseInt(bidIdStr);
        int projectId = Integer.parseInt(projectIdStr);

        Project project = projectDao.findById(projectId);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        Bid bid = bidDao.findById(bidId);
        if (bid == null || !"pending".equals(bid.getStatus())) {
            resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
            return;
        }

        // 选中该竞标，拒绝其他竞标
        bidDao.updateStatus(bidId, "accepted");
        for (Bid other : bidDao.findByProjectId(projectId)) {
            if (other.getId() != bidId && "pending".equals(other.getStatus())) {
                bidDao.updateStatus(other.getId(), "rejected");
            }
        }

        // 更新项目状态
        projectDao.updateStatus(projectId, "in_progress");

        // 创建订单
        Order order = new Order();
        order.setProjectId(projectId);
        order.setEmployerId(loginUser.getId());
        order.setFreelancerId(bid.getFreelancerId());
        order.setAmount(bid.getAmount());
        orderDao.insert(order);

        resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
    }
}
