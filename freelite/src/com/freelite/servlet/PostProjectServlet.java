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

@WebServlet("/project/post")
public class PostProjectServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Category> categories = categoryDao.findAll();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/B-project/postProject.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String budgetStr = req.getParameter("budget");
        String deadlineStr = req.getParameter("deadline");
        String categoryIdStr = req.getParameter("categoryId");

        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("error", "请输入项目标题");
            List<Category> categories = categoryDao.findAll();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/B-project/postProject.jsp").forward(req, resp);
            return;
        }

        Project project = new Project();
        project.setTitle(title.trim());
        project.setDescription(description);
        try {
            project.setBudget(Double.parseDouble(budgetStr));
        } catch (NumberFormatException e) {
            project.setBudget(0);
        }
        if (deadlineStr != null && !deadlineStr.trim().isEmpty()) {
            project.setDeadline(LocalDate.parse(deadlineStr));
        }
        try {
            project.setCategoryId(Integer.parseInt(categoryIdStr));
        } catch (NumberFormatException e) { /* ignore */ }
        project.setEmployerId(user.getId());

        projectDao.insert(project);
        resp.sendRedirect(req.getContextPath() + "/my/projects");
    }
}
