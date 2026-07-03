package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Category> categories = categoryDao.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/chen_kai_bo/postProject.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String budgetStr = request.getParameter("budget");
        String deadlineStr = request.getParameter("deadline");
        String categoryIdStr = request.getParameter("categoryId");

        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setEmployerId(user.getId());
        project.setStatus("open");
        project.setCreatedAt(LocalDateTime.now());

        if (budgetStr != null && !budgetStr.trim().isEmpty()) {
            try {
                project.setBudget(Double.parseDouble(budgetStr));
            } catch (NumberFormatException e) {
                project.setBudget(0.0);
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

        projectDao.insert(project);
        response.sendRedirect(request.getContextPath() + "/projects");
    }
}
