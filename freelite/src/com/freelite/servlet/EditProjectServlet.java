package com.freelite.servlet;

import com.freelite.dao.CategoryDao;
import com.freelite.dao.ProjectDao;
import com.freelite.model.Category;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/project/edit")
public class EditProjectServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
            return;
        }

        Project project = projectDao.findById(Integer.parseInt(idStr));
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
            return;
        }

        List<Category> categories = categoryDao.findAll();
        req.setAttribute("project", project);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/B-project/editProject.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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

        Project project = projectDao.findById(Integer.parseInt(idStr));
        if (project == null || project.getEmployerId() != user.getId()) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        if (title != null && !title.trim().isEmpty()) {
            project.setTitle(title.trim());
        }
        project.setDescription(description);

        try {
            String budgetStr = req.getParameter("budget");
            if (budgetStr != null && !budgetStr.trim().isEmpty()) {
                project.setBudget(Double.parseDouble(budgetStr));
            }
        } catch (NumberFormatException e) { /* ignore */ }

        String deadlineStr = req.getParameter("deadline");
        if (deadlineStr != null && !deadlineStr.trim().isEmpty()) {
            project.setDeadline(LocalDate.parse(deadlineStr));
        }

        String categoryIdStr = req.getParameter("categoryId");
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try {
                project.setCategoryId(Integer.parseInt(categoryIdStr));
            } catch (NumberFormatException e) { /* ignore */ }
        }

        projectDao.update(project);
        resp.sendRedirect(req.getContextPath() + "/project/detail?id=" + project.getId());
    }
}
