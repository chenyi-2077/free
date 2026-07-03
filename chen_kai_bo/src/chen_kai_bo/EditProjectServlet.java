package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class EditProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Project project = projectDao.findById(id);
            if (project == null) {
                response.sendRedirect(request.getContextPath() + "/my/projects");
                return;
            }

            List<Category> categories = categoryDao.findAll();
            request.setAttribute("project", project);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/chen_kai_bo/editProject.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Project project = projectDao.findById(id);
            if (project == null) {
                response.sendRedirect(request.getContextPath() + "/my/projects");
                return;
            }

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String budgetStr = request.getParameter("budget");
            String deadlineStr = request.getParameter("deadline");
            String categoryIdStr = request.getParameter("categoryId");
            String status = request.getParameter("status");

            if (title != null) project.setTitle(title);
            if (description != null) project.setDescription(description);
            if (budgetStr != null && !budgetStr.trim().isEmpty()) {
                try {
                    project.setBudget(Double.parseDouble(budgetStr));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
            if (deadlineStr != null && !deadlineStr.trim().isEmpty()) {
                project.setDeadline(LocalDate.parse(deadlineStr));
            }
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                try {
                    project.setCategoryId(Integer.parseInt(categoryIdStr));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
            if (status != null && !status.trim().isEmpty()) {
                project.setStatus(status);
            }

            projectDao.update(project);
            response.sendRedirect(request.getContextPath() + "/project/detail?id=" + id);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
        }
    }
}
