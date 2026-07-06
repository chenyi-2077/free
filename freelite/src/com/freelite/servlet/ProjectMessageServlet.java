package com.freelite.servlet;

import com.freelite.dao.ProjectMessageDao;
import com.freelite.model.ProjectMessage;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 项目消息 — 发送和查看
 * /project/message?projectId=X (GET 查看)
 * /project/message (POST 发送)
 */
public class ProjectMessageServlet extends HttpServlet {

    private ProjectMessageDao messageDao = new ProjectMessageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        List<ProjectMessage> messages = messageDao.findByProjectId(projectId);

        req.setAttribute("messages", messages);
        req.setAttribute("projectId", projectId);
        req.getRequestDispatcher("/projectMessages.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        String content = req.getParameter("content");

        if (projectIdStr == null || projectIdStr.isEmpty() || content == null || content.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);

        ProjectMessage msg = new ProjectMessage();
        msg.setProjectId(projectId);
        msg.setSenderId(loginUser.getId());
        msg.setContent(content.trim());

        messageDao.insert(msg);
        resp.sendRedirect(req.getContextPath() + "/project/message?projectId=" + projectId);
    }
}
