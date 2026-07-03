package chen_zi_hao;

import chen_zi_hao.OrderDao;
import chen_zi_hao.ReviewDao;
import chen_zi_hao.Order;
import chen_zi_hao.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/order/detail")
public class OrderDetailServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();
    private ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDao.findById(Integer.parseInt(idStr));
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        List<Review> reviews = reviewDao.findByOrderId(order.getId());

        chen_yi_an.User loginUser = (chen_yi_an.User) req.getSession().getAttribute("user");
        boolean canReview = loginUser != null && order.getStatus().equals("completed")
            && reviewDao.findByOrderId(order.getId()).isEmpty();
        boolean isEmployer = loginUser != null && loginUser.getId() == order.getEmployerId();

        req.setAttribute("order", order);
        req.setAttribute("reviews", reviews);
        req.setAttribute("canReview", canReview);
        req.setAttribute("isEmployer", isEmployer);
        req.getRequestDispatcher("/D-order/orderDetail.jsp").forward(req, resp);
    }
}
