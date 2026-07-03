package com.freelite.servlet;

import com.freelite.dao.ProjectDao;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/my/projects")
public class MyProjectsServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            req.setAttribute("projects", null);
            req.getRequestDispatcher("/B-project/myProjects.jsp").forward(req, resp);
            return;
        }

        List<Project> projects = projectDao.findByEmployerId(user.getId());
        req.setAttribute("projects", projects);
        req.getRequestDispatcher("/B-project/myProjects.jsp").forward(req, resp);
    }
}
