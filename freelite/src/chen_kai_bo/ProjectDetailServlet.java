package chen_kai_bo;
import chen_xi_rui.BidDao;

import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import chen_xi_rui.BidDao;
import chen_yi_an.User;
import chen_xi_rui.Bid;

public class ProjectDetailServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        try {
            int projectId = Integer.parseInt(pathInfo.replace("/", ""));
            Project project = projectDao.findById(projectId);
            if (project == null) {
                resp.sendRedirect(req.getContextPath() + "/projects");
                return;
            }

            req.setAttribute("project", project);
            req.setAttribute("bids", bidDao.findByProjectId(projectId));
            req.setAttribute("isOwner", loginUser.getId() == project.getEmployerId());
            req.getRequestDispatcher("/B-project/projectDetail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/projects");
        }
    }
}
