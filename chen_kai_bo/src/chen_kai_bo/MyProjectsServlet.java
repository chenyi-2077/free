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
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Project> projects = projectDao.findByEmployerId(user.getId());
        request.setAttribute("projects", projects);
        request.getRequestDispatcher("/chen_kai_bo/myProjects.jsp").forward(request, response);
    }
}
