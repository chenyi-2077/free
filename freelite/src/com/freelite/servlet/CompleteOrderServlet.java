package com.freelite.servlet;

import com.freelite.dao.OrderDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order/complete")
public class CompleteOrderServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        int id = Integer.parseInt(idStr);
        orderDao.updateStatus(id, "awaiting_confirm");
        resp.sendRedirect(req.getContextPath() + "/order/detail?id=" + id);
    }
}
