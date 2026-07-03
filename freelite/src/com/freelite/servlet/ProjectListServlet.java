package com.freelite.servlet;

import com.freelite.dao.CategoryDao;
import com.freelite.dao.ProjectDao;
import com.freelite.model.Category;
import com.freelite.model.Project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/projects")
public class ProjectListServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String catStr = req.getParameter("categoryId");
        Integer categoryId = null;
        if (catStr != null && !catStr.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(catStr);
            } catch (NumberFormatException e) { /* ignore */ }
        }

        List<Project> projects;
        if ((keyword != null && !keyword.trim().isEmpty()) || (categoryId != null && categoryId > 0)) {
            projects = projectDao.search(keyword, categoryId);
        } else {
            projects = projectDao.findAll();
        }

        List<Category> categories = categoryDao.findAll();

        req.setAttribute("projects", projects);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/B-project/projectList.jsp").forward(req, resp);
    }
}
