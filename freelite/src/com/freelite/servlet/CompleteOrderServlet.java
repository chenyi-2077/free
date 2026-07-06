package com.freelite.servlet;

import com.freelite.dao.OrderDao;
import com.freelite.model.User;
import com.freelite.service.EscrowService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自由职业者标记完成订单 → 状态变为 awaiting_confirm
 * 雇主确认后才真正释放资金
 */
public class CompleteOrderServlet extends HttpServlet {

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

        String orderIdStr = req.getParameter("orderId");
        int orderId = Integer.parseInt(orderIdStr);

        com.freelite.model.Order order = orderDao.findById(orderId);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // 自由职业者标记完成
        if (loginUser.getId() == order.getFreelancerId() && "in_progress".equals(order.getStatus())) {
            orderDao.updateStatus(orderId, "awaiting_confirm");
            req.getSession().setAttribute("successMsg", "📋 已标记完成，等待雇主确认。");
        }

        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
