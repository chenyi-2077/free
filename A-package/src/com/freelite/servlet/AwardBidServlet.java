package com.freelite.servlet;

import com.freelite.dao.*;
import com.freelite.model.*;
import com.freelite.service.EscrowService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AwardBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();
    private ProjectDao projectDao = new ProjectDao();
    private OrderDao orderDao = new OrderDao();
    private EscrowService escrowService = new EscrowService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

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

        // ===== 结算步骤1: 资金托管（冻结雇主余额）=====
        double escrowAmount = bid.getAmount();
        if (!escrowService.fundEscrow(projectId, loginUser.getId(), escrowAmount)) {
            req.getSession().setAttribute("errorMsg", "❌ 授标失败：钱包余额不足，请先充值。项目预算为 ¥" + String.format("%.2f", escrowAmount));
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

        // 创建订单（含托管金额）
        Order order = new Order();
        order.setProjectId(projectId);
        order.setEmployerId(loginUser.getId());
        order.setFreelancerId(bid.getFreelancerId());
        order.setAmount(bid.getAmount());
        orderDao.insert(order);

        req.getSession().setAttribute("successMsg", "🎉 中标成功！¥" + String.format("%.2f", escrowAmount) + " 已托管担保，自由职业者可开始工作。");
        resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
    }
}
