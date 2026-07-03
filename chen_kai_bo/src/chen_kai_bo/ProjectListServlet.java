package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProjectListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String categoryIdStr = request.getParameter("categoryId");

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        List<Project> projects;
        if ((keyword != null && !keyword.trim().isEmpty()) || (categoryId != null && categoryId > 0)) {
            projects = projectDao.search(keyword, categoryId);
        } else {
            projects = projectDao.findAll();
        }

        List<Category> categories = categoryDao.findAll();

        request.setAttribute("projects", projects);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedKeyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);

        request.getRequestDispatcher("/chen_kai_bo/projectList.jsp").forward(request, response);
    }
}
