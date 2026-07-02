package com.freelite.servlet;

import com.freelite.dao.OrderDao;
import com.freelite.dao.ReviewDao;
import com.freelite.model.Order;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderDetailServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();
    private ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(pathInfo.replace("/", ""));
            Order order = orderDao.findById(orderId);
            if (order == null) {
                resp.sendRedirect(req.getContextPath() + "/orders");
                return;
            }

            boolean isEmployer = loginUser.getId() == order.getEmployerId();

            // 订单已完成且该用户还未评价过时显示评价表单
            boolean canReview = false;
            if ("completed".equals(order.getStatus())) {
                // 所有参与方都可评价（简化处理：当前用户没评过即可）
                com.freelite.model.Review existing = reviewDao.findByOrderId(orderId);
                if (existing == null) {
                    canReview = true;
                }
            }

            req.setAttribute("order", order);
            req.setAttribute("canReview", canReview);
            req.setAttribute("isEmployer", isEmployer);
            req.getRequestDispatcher("/D-order/orderDetail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }
}
