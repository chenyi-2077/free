package chen_xi_rui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 提交竞标（自由职业者报价）
 */
public class PlaceBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 无登录场景：模拟一个自由职业者用户
            user = new User(2, "自由职业者", "freelancer", 4.5);
            session.setAttribute("user", user);
        }

        String projectIdParam = request.getParameter("projectId");
        String amountParam = request.getParameter("amount");
        String daysParam = request.getParameter("days");
        String proposal = request.getParameter("proposal");

        if (projectIdParam == null || amountParam == null || daysParam == null || proposal == null ||
            projectIdParam.trim().isEmpty() || amountParam.trim().isEmpty() ||
            daysParam.trim().isEmpty() || proposal.trim().isEmpty()) {
            request.setAttribute("error", "所有字段均为必填");
            request.setAttribute("projectId", Integer.parseInt(projectIdParam));
            request.getRequestDispatcher("/chen_xi_rui/bidForm.jsp").forward(request, response);
            return;
        }

        Bid bid = new Bid();
        bid.setProjectId(Integer.parseInt(projectIdParam));
        bid.setFreelancerId(user.getId());
        bid.setAmount(Double.parseDouble(amountParam));
        bid.setDays(Integer.parseInt(daysParam));
        bid.setProposal(proposal);
        bid.setStatus("pending");

        bidDao.insert(bid);

        response.sendRedirect(request.getContextPath() + "/bids?projectId=" + projectIdParam);
    }
}
