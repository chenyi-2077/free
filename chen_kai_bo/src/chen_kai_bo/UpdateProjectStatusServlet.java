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
        String idStr = request.getParameter("id");
        String status = request.getParameter("status");

        if (idStr != null && !idStr.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                projectDao.updateStatus(id, status);
                response.sendRedirect(request.getContextPath() + "/project/detail?id=" + id);
                return;
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        response.sendRedirect(request.getContextPath() + "/projects");
    }
}
