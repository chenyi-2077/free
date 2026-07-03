package chen_zi_hao;

import chen_zi_hao.OrderDao;
import chen_zi_hao.Order;
import chen_yi_an.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order/complete")
public class CompleteOrderServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDao.findById(Integer.parseInt(idStr));
        if (order != null && order.getFreelancerId() == user.getId()) {
            orderDao.updateStatus(order.getId(), "completed");
        }

        resp.sendRedirect(req.getContextPath() + "/order/detail?id=" + idStr);
    }
}
