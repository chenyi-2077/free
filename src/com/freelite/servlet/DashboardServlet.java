package com.freelite.servlet;

import com.freelite.dao.OrderDAO;
import com.freelite.dao.ProjectDAO;
import com.freelite.dao.UserDAO;
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
 * 数据看板 Servlet（答辩亮点）
 * D负责
 * 
 * GET /dashboard → 统计展示
 */
public class DashboardServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 统计
        List<Order> allOrders = orderDAO.findAll();
        int totalOrders = allOrders.size();
        int completedOrders = 0;
        int inProgressOrders = 0;

        for (Order o : allOrders) {
            if ("completed".equals(o.getStatus())) completedOrders++;
            else if ("in_progress".equals(o.getStatus())) inProgressOrders++;
        }

        // 最近5个订单
        List<Order> recentOrders = allOrders.size() > 5 ? allOrders.subList(0, 5) : allOrders;

        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("completedOrders", completedOrders);
        req.setAttribute("inProgressOrders", inProgressOrders);
        req.setAttribute("recentOrders", recentOrders);

        req.getRequestDispatcher("/D-order/dashboard.jsp").forward(req, resp);
    }
}
