package chen_xi_rui;

import chen_yi_an.User;
import chen_kai_bo.Project;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class MyBidsServlet extends HttpServlet {
    private BidDao bidDao = new BidDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("bids", bidDao.findByFreelancerId(loginUser.getId()));
        req.getRequestDispatcher("/C-bid/myBids.jsp").forward(req, resp);
    }
}
