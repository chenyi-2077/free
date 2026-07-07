package chen_xi_rui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 查看竞标列表
 */
public class BidListServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String projectIdParam = request.getParameter("projectId");
        if (projectIdParam != null && !projectIdParam.trim().isEmpty()) {
            int projectId = Integer.parseInt(projectIdParam);
            request.setAttribute("projectId", projectId);
            request.setAttribute("bids", bidDao.findByProjectId(projectId));
            request.getRequestDispatcher("/chen_xi_rui/bidsOnProject.jsp").forward(request, response);
        } else {
            request.setAttribute("bids", bidDao.findAll());
            request.getRequestDispatcher("/chen_xi_rui/bidsOnProject.jsp").forward(request, response);
        }
    }
}
