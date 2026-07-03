package com.freelite.servlet;

import com.freelite.dao.ReviewDao;
import com.freelite.model.Review;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        req.setAttribute("orderId", Integer.parseInt(orderIdStr));
        req.getRequestDispatcher("/D-order/review.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        String toUserIdStr = req.getParameter("toUserId");
        String scoreStr = req.getParameter("score");
        String comment = req.getParameter("comment");

        if (orderIdStr == null || toUserIdStr == null || scoreStr == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Review review = new Review();
        review.setOrderId(Integer.parseInt(orderIdStr));
        review.setFromUserId(user.getId());
        review.setToUserId(Integer.parseInt(toUserIdStr));
        review.setScore(Integer.parseInt(scoreStr));
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        reviewDao.insert(review);
        resp.sendRedirect(req.getContextPath() + "/order/detail?id=" + orderIdStr);
    }
}
