package com.freelite.servlet;

import com.freelite.dao.ProjectDao;
import com.freelite.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/project/status")
public class UpdateProjectStatusServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        String status = req.getParameter("status");

        if (idStr == null || status == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        projectDao.updateStatus(Integer.parseInt(idStr), status);
        resp.sendRedirect(req.getContextPath() + "/project/detail?id=" + idStr);
    }
}
