package com.freelite.servlet;

import com.freelite.dao.OrderDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = loginUser.getId();
        int totalOrders = orderDao.countByUserId(userId);
        int completedOrders = orderDao.countByUserId(userId, "completed");
        int inProgressOrders = orderDao.countByUserId(userId, "in_progress");

        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("completedOrders", completedOrders);
        req.setAttribute("inProgressOrders", inProgressOrders);
        req.setAttribute("recentOrders", orderDao.findRecentByUserId(userId, 5));
        req.getRequestDispatcher("/D-order/dashboard.jsp").forward(req, resp);
    }
}
