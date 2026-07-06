package com.freelite.servlet;

import com.freelite.dao.*;
import com.freelite.model.*;
import com.freelite.service.EscrowService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UpdateProjectStatusServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private OrderDao orderDao = new OrderDao();
    private BidDao bidDao = new BidDao();
    private EscrowService escrowService = new EscrowService();

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
        String newStatus = req.getParameter("status");
        String redirect = req.getParameter("redirect");

        Project project = projectDao.findById(projectId);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        // 校验状态转换是否合法
        String current = project.getStatus();
        boolean valid = false;
        switch (newStatus) {
            case "cancelled":
                if ("open".equals(current) || "in_progress".equals(current)) valid = true;
                break;
            case "open":
                if ("cancelled".equals(current)) valid = true;
                break;
        }

        if (!valid) {
            resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
            return;
        }

        projectDao.updateStatus(projectId, newStatus);

        // 取消/恢复时处理关联订单
        List<Order> orders = orderDao.findByProject(projectId);
        if (orders != null) {
            for (Order order : orders) {
                if ("cancelled".equals(newStatus)) {
                    // 取消项目：取消进行中/等待确认的订单 + 退冻结资金
                    String orderStatus = order.getStatus();
                    if ("in_progress".equals(orderStatus) || "awaiting_confirm".equals(orderStatus)) {
                        double escrowAmount = order.getEscrowAmount();
                        if (escrowAmount > 0) {
                            escrowService.refundToEmployer(projectId, order.getEmployerId(), escrowAmount);
                        }
                        orderDao.updateStatus(order.getId(), "cancelled");
                    }
                } else if ("open".equals(newStatus)) {
                    // 重新开放：清理之前的所有订单
                    orderDao.updateStatus(order.getId(), "cancelled");
                }
            }
        }

        // 重新开放时：把已中标的竞标改回 pending
        if ("open".equals(newStatus)) {
            List<com.freelite.model.Bid> bids = bidDao.findByProjectId(projectId);
            if (bids != null) {
                for (com.freelite.model.Bid b : bids) {
                    if ("accepted".equals(b.getStatus())) {
                        bidDao.updateStatus(b.getId(), "pending");
                    }
                }
            }
        }

        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + redirect);
        } else {
            resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
        }
    }
}
