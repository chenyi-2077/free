package com.freelite.servlet;

import com.freelite.dao.OrderDAO;
import com.freelite.dao.ReviewDAO;
import com.freelite.model.Order;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 订单详情 Servlet
 * D负责
 * 
 * GET /order/123 → 订单详情
 */
public class OrderDetailServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        int orderId = Integer.parseInt(pathInfo.replace("/", ""));
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(404);
            return;
        }

        boolean isOwnerOrFreelancer = (order.getEmployerId() == user.getId() || order.getFreelancerId() == user.getId());

        req.setAttribute("order", order);
        req.setAttribute("canReview", "completed".equals(order.getStatus()) && !reviewDAO.hasReviewed(orderId));
        req.setAttribute("isEmployer", order.getEmployerId() == user.getId());

        if (!isOwnerOrFreelancer) {
            resp.sendError(403);
            return;
        }

        req.getRequestDispatcher("/D-order/orderDetail.jsp").forward(req, resp);
    }
}
