package chen_xi_rui;

import chen_xi_rui.BidDao;
import chen_xi_rui.Bid;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Project;
import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bid/place")
public class PlaceBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();
    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        Project project = projectDao.findById(Integer.parseInt(projectIdStr));
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        req.setAttribute("project", project);
        req.getRequestDispatcher("/C-bid/bidForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        String amountStr = req.getParameter("amount");
        String daysStr = req.getParameter("days");
        String proposal = req.getParameter("proposal");

        if (projectIdStr == null || amountStr == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        Bid bid = new Bid();
        bid.setProjectId(Integer.parseInt(projectIdStr));
        bid.setFreelancerId(user.getId());
        try {
            bid.setAmount(Double.parseDouble(amountStr));
        } catch (NumberFormatException e) {
            bid.setAmount(0);
        }
        try {
            bid.setDays(Integer.parseInt(daysStr));
        } catch (NumberFormatException e) {
            bid.setDays(0);
        }
        bid.setProposal(proposal);

        bidDao.insert(bid);
        resp.sendRedirect(req.getContextPath() + "/project/detail?id=" + projectIdStr);
    }
}
