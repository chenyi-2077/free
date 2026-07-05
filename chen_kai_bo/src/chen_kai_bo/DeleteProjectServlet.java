package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();

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
        projectDao.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/my/projects");
    }
}
