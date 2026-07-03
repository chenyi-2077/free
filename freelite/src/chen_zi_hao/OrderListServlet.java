package chen_zi_hao;

import chen_zi_hao.OrderDao;
import chen_zi_hao.Order;
import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<Order> orders = null;

        if (user != null) {
            // show both as employer and freelancer
            List<Order> asEmployer = orderDao.findByEmployerId(user.getId());
            List<Order> asFreelancer = orderDao.findByFreelancerId(user.getId());
            asEmployer.addAll(asFreelancer);
            orders = asEmployer;
        } else {
            orders = orderDao.findAll();
        }

        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/D-order/orderList.jsp").forward(req, resp);
    }
}
