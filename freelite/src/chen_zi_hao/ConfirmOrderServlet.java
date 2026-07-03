package chen_zi_hao;

import chen_zi_hao.OrderDao;
import chen_zi_hao.Order;
import chen_yi_an.WalletDao;
import chen_yi_an.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order/confirm")
public class ConfirmOrderServlet extends HttpServlet {

    private OrderDao orderDao = new OrderDao();
    private WalletDao walletDao = new WalletDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("orderId");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDao.findById(Integer.parseInt(idStr));
        if (order == null || order.getEmployerId() != user.getId()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // Release frozen funds to freelancer
        walletDao.release(user.getId(), order.getAmount());
        // Actually transfer: freeze was on employer -> now release goes to employer balance
        // We need to deduct from employer and credit freelancer
        walletDao.payment(user.getId(), order.getAmount(), "订单付款 订单#" + order.getId());
        walletDao.income(order.getFreelancerId(), order.getAmount(), "订单收入 订单#" + order.getId());

        orderDao.updateStatus(order.getId(), "confirmed");

        resp.sendRedirect(req.getContextPath() + "/order/detail?id=" + idStr);
    }
}
