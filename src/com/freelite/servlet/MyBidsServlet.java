package com.freelite.servlet;

import com.freelite.dao.BidDAO;
import com.freelite.model.Bid;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 我的竞标列表 Servlet（自由职业者查看）
 * C负责
 * 
 * GET /my/bids → 我投的所有竞标
 */
public class MyBidsServlet extends HttpServlet {

    private BidDAO bidDAO = new BidDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Bid> bids = bidDAO.findByFreelancer(user.getId());
        req.setAttribute("bids", bids);
        req.getRequestDispatcher("/C-bid/myBids.jsp").forward(req, resp);
    }
}
