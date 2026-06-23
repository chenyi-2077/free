package com.freelite.servlet;

import com.freelite.dao.*;
import com.freelite.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReviewServlet extends HttpServlet {

    private ReviewDao reviewDao = new ReviewDao();
    private OrderDao orderDao = new OrderDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        String scoreStr = req.getParameter("score");
        String comment = req.getParameter("comment");

        int orderId = Integer.parseInt(orderIdStr);
        int score = Integer.parseInt(scoreStr);

        Order order = orderDao.findById(orderId);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // 确定评价对象
        int toUserId;
        if (loginUser.getId() == order.getEmployerId()) {
            toUserId = order.getFreelancerId();
        } else {
            toUserId = order.getEmployerId();
        }

        Review review = new Review();
        review.setOrderId(orderId);
        review.setFromUserId(loginUser.getId());
        review.setToUserId(toUserId);
        review.setScore(score);
        review.setComment(comment);

        reviewDao.insert(review);

        // 更新被评价者的评分
        userDao.updateRating(toUserId);

        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
