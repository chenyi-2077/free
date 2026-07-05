package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }
        int id = Integer.parseInt(idStr);
        Project project = projectDao.findById(id);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }
        request.setAttribute("project", project);
        request.setAttribute("categories", categoryDao.findAll());
        request.getRequestDispatcher("/chen_kai_bo/editProject.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String budgetStr = request.getParameter("budget");
        String deadlineStr = request.getParameter("deadline");
        String categoryIdStr = request.getParameter("categoryId");

        if (idStr == null || title == null || title.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }

        int id = Integer.parseInt(idStr);
        Project project = projectDao.findById(id);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }

        project.setTitle(title.trim());
        project.setDescription(description);
        if (budgetStr != null && !budgetStr.isEmpty()) {
            project.setBudget(Double.parseDouble(budgetStr));
        }
        if (deadlineStr != null && !deadlineStr.isEmpty()) {
            project.setDeadline(java.sql.Date.valueOf(deadlineStr).toLocalDate());
        }
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            project.setCategoryId(Integer.parseInt(categoryIdStr));
        }

        projectDao.update(project);
        response.sendRedirect(request.getContextPath() + "/my/projects");
    }
}
