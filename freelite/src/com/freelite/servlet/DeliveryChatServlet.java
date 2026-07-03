package com.freelite.servlet;

import com.freelite.dao.DeliveryDao;
import com.freelite.dao.ProjectMessageDao;
import com.freelite.model.Delivery;
import com.freelite.model.ProjectMessage;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/deliveryChat")
public class DeliveryChatServlet extends HttpServlet {

    private DeliveryDao deliveryDao = new DeliveryDao();
    private ProjectMessageDao messageDao = new ProjectMessageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        List<Delivery> deliveries = deliveryDao.findByProjectId(projectId);
        List<ProjectMessage> messages = messageDao.findByProjectId(projectId);

        req.setAttribute("projectId", projectId);
        req.setAttribute("deliveries", deliveries);
        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/A-user/deliveryChat.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        int projectId = Integer.parseInt(req.getParameter("projectId"));

        if ("message".equals(action)) {
            String content = req.getParameter("content");
            if (content != null && !content.trim().isEmpty()) {
                ProjectMessage msg = new ProjectMessage();
                msg.setProjectId(projectId);
                msg.setSenderId(user.getId());
                msg.setContent(content.trim());
                messageDao.insert(msg);
            }
        } else if ("delivery".equals(action)) {
            String content = req.getParameter("content");
            Delivery delivery = new Delivery();
            delivery.setProjectId(projectId);
            delivery.setSenderId(user.getId());
            delivery.setContent(content != null ? content.trim() : "");
            delivery.setFileName(req.getParameter("fileName"));
            delivery.setFilePath(req.getParameter("filePath"));
            deliveryDao.insert(delivery);
        }

        resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
    }
}
