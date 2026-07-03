package chen_xi_rui;

import chen_xi_rui.BidDao;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Project;
import chen_xi_rui.Bid;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/bids")
public class BidListServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();
    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        Project project = projectDao.findById(projectId);
        List<Bid> bids = bidDao.findByProjectId(projectId);

        req.setAttribute("project", project);
        req.setAttribute("bids", bids);
        req.getRequestDispatcher("/C-bid/bidsOnProject.jsp").forward(req, resp);
    }
}
