package com.freelite.servlet;

import com.freelite.dao.OrderDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.freelite.util.AuthUtil;

public class CompleteOrderServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = AuthUtil.requireLogin(req, resp);
        if (loginUser == null) return;

        String orderIdStr = req.getParameter("orderId");
        int orderId = Integer.parseInt(orderIdStr);

        com.freelite.model.Order order = orderDao.findById(orderId);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // 只有自由职业者可以标记完成
        if (loginUser.getId() == order.getFreelancerId() && "in_progress".equals(order.getStatus())) {
            orderDao.updateStatus(orderId, "completed");
        }

        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
