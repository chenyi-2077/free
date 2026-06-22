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
 * 评价 Servlet
 * D负责
 * 
 * POST /review → 提交评价
 */
public class ReviewServlet extends HttpServlet {

    private ReviewDAO reviewDAO = new ReviewDAO();
    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int score = Integer.parseInt(req.getParameter("score"));
        String comment = req.getParameter("comment");

        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(404);
            return;
        }

        // 确定评价对象
        int toUserId = (order.getEmployerId() == user.getId())
                ? order.getFreelancerId()
                : order.getEmployerId();

        reviewDAO.insert(orderId, user.getId(), toUserId, score, comment);
        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
