package chen_xi_rui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlaceBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 未登录可查看竞标表单
        String projectIdParam = request.getParameter("projectId");
        if (projectIdParam == null || projectIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        request.setAttribute("projectId", Integer.parseInt(projectIdParam));
        request.getRequestDispatcher("/chen_xi_rui/bidForm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String projectIdParam = request.getParameter("projectId");
        String amountParam = request.getParameter("amount");
        String daysParam = request.getParameter("days");
        String proposal = request.getParameter("proposal");

        if (projectIdParam == null || amountParam == null || daysParam == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        int projectId = Integer.parseInt(projectIdParam);
        double amount = Double.parseDouble(amountParam);
        int days = Integer.parseInt(daysParam);

        Bid bid = new Bid();
        bid.setProjectId(projectId);
        bid.setFreelancerId(loginUser.getId());
        bid.setAmount(amount);
        bid.setDays(days);
        bid.setProposal(proposal != null ? proposal : "");
        bid.setStatus("pending");

        bidDao.insert(bid);

        response.sendRedirect(request.getContextPath() + "/projects");
    }
}
