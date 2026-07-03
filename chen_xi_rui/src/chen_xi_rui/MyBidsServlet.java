package chen_xi_rui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 查看我的竞标记录
 */
public class MyBidsServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 无登录场景：模拟一个自由职业者用户
            user = new User(2, "自由职业者", "freelancer", 4.5);
            session.setAttribute("user", user);
        }

        List<Bid> bids = bidDao.findByFreelancerId(user.getId());
        request.setAttribute("bids", bids);
        request.getRequestDispatcher("/chen_xi_rui/myBids.jsp").forward(request, response);
    }
}
