package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class MyProjectsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 独立版本：session有 user 就用，无则查雇主ID=0（演示用）
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        List<Project> projects;
        if (user != null) {
            projects = projectDao.findByEmployerId(user.getId());
        } else {
            projects = projectDao.findAll();
        }

        request.setAttribute("projects", projects);
        request.getRequestDispatcher("/chen_kai_bo/myProjects.jsp").forward(request, response);
    }
}
