package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateProjectStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        String status = request.getParameter("status");
        String redirect = request.getParameter("redirect");

        if (idStr == null || status == null) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }

        int id = Integer.parseInt(idStr);
        Project project = projectDao.findById(id);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/my/projects");
            return;
        }

        projectDao.updateStatus(id, status);

        String target = (redirect != null && !redirect.isEmpty()) ? redirect : "/my/projects";
        response.sendRedirect(request.getContextPath() + target);
    }
}
