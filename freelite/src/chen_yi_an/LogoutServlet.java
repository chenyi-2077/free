package chen_yi_an;
import chen_kai_bo.Project;
import chen_kai_bo.Bid;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Category;
import chen_xi_rui.BidDao;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
