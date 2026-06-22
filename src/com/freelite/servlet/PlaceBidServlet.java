package com.freelite.servlet;

import com.freelite.dao.BidDAO;
import com.freelite.dao.ProjectDAO;
import com.freelite.model.Bid;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 投递竞标 Servlet
 * C负责
 * 
 * GET  /bid/place?projectId=123 → 显示竞标表单
 * POST /bid/place              → 处理投递
 */
public class PlaceBidServlet extends HttpServlet {

    private BidDAO bidDAO = new BidDAO();
    private ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        Project project = projectDAO.findById(projectId);
        if (project == null) {
            resp.sendError(404);
            return;
        }

        // 检查是否已投递
        if (bidDAO.hasBid(projectId, user.getId())) {
            req.setAttribute("error", "你已经投递过该项目");
            req.setAttribute("project", project);
            req.getRequestDispatcher("/C-bid/bidForm.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("project", project);
        req.getRequestDispatcher("/C-bid/bidForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int projectId = Integer.parseInt(req.getParameter("projectId"));
        double amount = Double.parseDouble(req.getParameter("amount"));
        int days = Integer.parseInt(req.getParameter("days"));
        String proposal = req.getParameter("proposal");

        // 检查重复投递
        if (bidDAO.hasBid(projectId, user.getId())) {
            resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
            return;
        }

        Bid bid = new Bid();
        bid.setProjectId(projectId);
        bid.setFreelancerId(user.getId());
        bid.setAmount(amount);
        bid.setDays(days);
        bid.setProposal(proposal);

        int id = bidDAO.insert(bid);
        if (id > 0) {
            resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
        } else {
            req.setAttribute("error", "投递失败");
            req.setAttribute("project", projectDAO.findById(projectId));
            req.getRequestDispatcher("/C-bid/bidForm.jsp").forward(req, resp);
        }
    }
}
