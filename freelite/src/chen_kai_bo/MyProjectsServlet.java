package chen_kai_bo;

import chen_kai_bo.ProjectDao;
import chen_kai_bo.Project;
import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/my/projects")
public class MyProjectsServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Project> projects = projectDao.findByEmployerId(user.getId());
        List<Project> bidded = projectDao.findByFreelancerId(user.getId());
        req.setAttribute("myProjects", projects);
        req.setAttribute("biddedProjects", bidded);
        req.getRequestDispatcher("/B-project/myProjects.jsp").forward(req, resp);
    }
}
