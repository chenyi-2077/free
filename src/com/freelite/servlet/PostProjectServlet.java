package com.freelite.servlet;

import com.freelite.dao.CategoryDAO;
import com.freelite.dao.ProjectDAO;
import com.freelite.model.Category;
import com.freelite.model.Project;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 发布项目 Servlet
 * B负责
 * 
 * GET  /project/post → 显示发布表单
 * POST /project/post → 处理发布
 */
public class PostProjectServlet extends HttpServlet {

    private ProjectDAO projectDAO = new ProjectDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Category> categories = categoryDAO.findAll();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/B-project/postProject.jsp").forward(req, resp);
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

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String budgetStr = req.getParameter("budget");
        String deadline = req.getParameter("deadline");
        String categoryStr = req.getParameter("categoryId");

        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("error", "项目标题不能为空");
            req.setAttribute("categories", categoryDAO.findAll());
            req.getRequestDispatcher("/B-project/postProject.jsp").forward(req, resp);
            return;
        }

        Project project = new Project();
        project.setTitle(title.trim());
        project.setDescription(description != null ? description.trim() : "");
        if (budgetStr != null && !budgetStr.isEmpty()) {
            try { project.setBudget(Double.parseDouble(budgetStr)); } catch (NumberFormatException ignored) {}
        }
        project.setDeadline(deadline);
        if (categoryStr != null && !categoryStr.isEmpty()) {
            try { project.setCategoryId(Integer.parseInt(categoryStr)); } catch (NumberFormatException ignored) {}
        }
        project.setEmployerId(user.getId());

        int id = projectDAO.insert(project);
        if (id > 0) {
            resp.sendRedirect(req.getContextPath() + "/project/" + id);
        } else {
            req.setAttribute("error", "发布失败");
            req.setAttribute("categories", categoryDAO.findAll());
            req.getRequestDispatcher("/B-project/postProject.jsp").forward(req, resp);
        }
    }
}
