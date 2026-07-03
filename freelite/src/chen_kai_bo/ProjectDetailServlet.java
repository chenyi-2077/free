package chen_kai_bo;

import chen_kai_bo.ProjectDao;
import chen_kai_bo.Project;
import chen_xi_rui.BidDao;
import chen_xi_rui.Bid;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/project/detail")
public class ProjectDetailServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(idParam);
        Project project = projectDao.findById(projectId);
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        List<Bid> bids = bidDao.findByProjectId(projectId);

        req.setAttribute("project", project);
        req.setAttribute("bids", bids);
        req.getRequestDispatcher("/B-project/projectDetail.jsp").forward(req, resp);
    }
}
