package com.freelite.servlet;

import com.freelite.dao.CategoryDao;
import com.freelite.dao.ProjectDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import com.freelite.util.AuthUtil;

public class PostProjectServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = AuthUtil.requireLogin(req, resp);
        if (loginUser == null) return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User loginUser = AuthUtil.requireLogin(req, resp);
        if (loginUser == null) return;
        String description = req.getParameter("description");
        String budgetStr = req.getParameter("budget");
        String deadlineStr = req.getParameter("deadline");
        String categoryIdStr = req.getParameter("categoryId");

        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("error", "项目标题不能为空");
            req.setAttribute("categories", categoryDao.findAll());
            req.getRequestDispatcher("/B-project/postProject.jsp").forward(req, resp);
            return;
        }

        com.freelite.model.Project p = new com.freelite.model.Project();
        p.setTitle(title.trim());
        p.setDescription(description);
        if (budgetStr != null && !budgetStr.isEmpty()) {
            p.setBudget(Double.parseDouble(budgetStr));
        }
        if (deadlineStr != null && !deadlineStr.isEmpty()) {
            p.setDeadline(LocalDate.parse(deadlineStr));
        }
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            p.setCategoryId(Integer.parseInt(categoryIdStr));
        }
        p.setEmployerId(loginUser.getId());

        projectDao.insert(p);
        resp.sendRedirect(req.getContextPath() + "/projects");
    }
}
