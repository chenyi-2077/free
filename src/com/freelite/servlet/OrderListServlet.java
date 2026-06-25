package com.freelite.servlet;

import com.freelite.dao.OrderDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.freelite.util.AuthUtil;

public class OrderListServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = AuthUtil.requireLogin(req, resp);
        if (loginUser == null) return;
        req.getRequestDispatcher("/D-order/orderList.jsp").forward(req, resp);
    }
}
