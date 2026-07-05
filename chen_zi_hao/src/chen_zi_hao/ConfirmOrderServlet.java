package chen_zi_hao;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfirmOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        int id = Integer.parseInt(idStr);
        Order order = orderDao.findById(id);
        if (order == null || order.getEmployerId() != loginUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        if (!"awaiting_confirm".equals(order.getStatus())) {
            response.sendRedirect(request.getContextPath() + "/order/detail?id=" + id);
            return;
        }
        orderDao.updateStatus(id, "completed");
        response.sendRedirect(request.getContextPath() + "/order/detail?id=" + id);
    }
}
