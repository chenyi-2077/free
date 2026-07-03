package chen_kai_bo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProjectDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/projects");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Project project = projectDao.findById(id);
            if (project == null) {
                response.sendRedirect(request.getContextPath() + "/projects");
                return;
            }

            HttpSession session = request.getSession(false);
            User user = (User) (session != null ? session.getAttribute("user") : null);
            boolean isOwner = (user != null && user.getId() == project.getEmployerId());

            request.setAttribute("project", project);
            request.setAttribute("isOwner", isOwner);
            request.setAttribute("currentUser", user);
            request.getRequestDispatcher("/chen_kai_bo/projectDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/projects");
        }
    }
}
