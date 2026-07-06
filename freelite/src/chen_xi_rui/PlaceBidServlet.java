package chen_xi_rui;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;
import chen_yi_an.EscrowService;
import chen_kai_bo.Project;

import chen_xi_rui.ProjectDao;
import chen_xi_rui.Project;
import chen_xi_rui.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class PlaceBidServlet extends HttpServlet {
    private ProjectDao projectDao = new ProjectDao();
    private com.freelite.dao.BidDao bidDao = new com.freelite.dao.BidDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
        int projectId = Integer.parseInt(projectIdStr);
        Project project = projectDao.findById(projectId);
        if (project == null) {
        req.setAttribute("project", project);
        req.getRequestDispatcher("/C-bid/bidForm.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        String amountStr = req.getParameter("amount");
        String daysStr = req.getParameter("days");
        String proposal = req.getParameter("proposal");
        // 雇主不能给自己的项目投竞标
        if (loginUser.getId() == project.getEmployerId()) {
            req.setAttribute("project", project);
            req.setAttribute("error", "您不能给自己的项目投递竞标");
            req.getRequestDispatcher("/C-bid/bidForm.jsp").forward(req, resp);
        com.freelite.model.Bid bid = new com.freelite.model.Bid();
        bid.setProjectId(projectId);
        bid.setFreelancerId(loginUser.getId());
        bid.setAmount(Double.parseDouble(amountStr));
        bid.setDays(Integer.parseInt(daysStr));
        bid.setProposal(proposal);
        bidDao.insert(bid);
        resp.sendRedirect(req.getContextPath() + "/project/" + projectId);
}
