package com.freelite.servlet;

import com.freelite.dao.ProjectDao;
import com.freelite.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/project/delete")
public class DeleteProjectServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
            return;
        }

        projectDao.deleteById(Integer.parseInt(idStr));
        resp.sendRedirect(req.getContextPath() + "/my/projects");
    }
}
