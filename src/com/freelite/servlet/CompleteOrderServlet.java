package com.freelite.servlet;

import com.freelite.dao.OrderDAO;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 完成订单 Servlet
 * D负责
 * 
 * POST /order/complete → 自由职业者标记完成
 */
public class CompleteOrderServlet extends HttpServlet {

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
        orderDAO.updateStatus(orderId, "completed");

        // 同时更新项目状态
        com.freelite.dao.ProjectDAO projectDAO = new com.freelite.dao.ProjectDAO();
        int projectId = orderDAO.findById(orderId).getProjectId();
        projectDAO.updateStatus(projectId, "completed");

        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
