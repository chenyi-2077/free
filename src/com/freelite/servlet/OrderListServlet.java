package com.freelite.servlet;

import com.freelite.dao.OrderDAO;
import com.freelite.model.Order;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 订单列表 Servlet
 * D负责
 * 
 * GET /orders → 我的订单列表
 */
public class OrderListServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Order> orders = orderDAO.findByUser(user.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/D-order/orderList.jsp").forward(req, resp);
    }
}
