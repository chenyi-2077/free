package chen_xi_rui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 我的竞标 / 所有竞标
 */
public class MyBidsServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);
        if (user != null) {
            request.setAttribute("bids", bidDao.findByFreelancerId(user.getId()));
        } else {
            request.setAttribute("bids", bidDao.findAll());
        }
        request.getRequestDispatcher("/chen_xi_rui/myBids.jsp").forward(request, response);
    }
}
