package chen_xi_rui;

import chen_xi_rui.BidDao;
import chen_xi_rui.Bid;
import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/my/bids")
public class MyBidsServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Bid> bids = bidDao.findByFreelancerId(user.getId());
        req.setAttribute("bids", bids);
        req.getRequestDispatcher("/C-bid/myBids.jsp").forward(req, resp);
    }
}
