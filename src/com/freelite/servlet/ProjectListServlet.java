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
 * 项目列表 Servlet
 * B负责
 * 
 * GET /projects → 项目列表（支持搜索+分页+分类筛选）
 */
public class ProjectListServlet extends HttpServlet {

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

        // 获取参数
        String keyword = req.getParameter("keyword");
        String catParam = req.getParameter("category");
        String pageParam = req.getParameter("page");

        int categoryId = 0;
        int page = 1;
        int pageSize = 10;

        if (catParam != null && !catParam.isEmpty()) {
            try { categoryId = Integer.parseInt(catParam); } catch (NumberFormatException e) {}
        }
        if (pageParam != null && !pageParam.isEmpty()) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException e) {}
        }

        // 查询
        List<Project> projects = projectDAO.search(keyword, categoryId, page, pageSize);
        int total = projectDAO.count(keyword, categoryId);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        List<Category> categories = categoryDAO.findAll();

        req.setAttribute("projects", projects);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("keyword", keyword);
        req.setAttribute("selectedCategory", categoryId);

        req.getRequestDispatcher("/B-project/projectList.jsp").forward(req, resp);
    }
}
