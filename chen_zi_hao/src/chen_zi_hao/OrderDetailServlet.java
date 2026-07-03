package chen_zi_hao;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDao();
    private ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        int id = Integer.parseInt(idStr);
        Order order = orderDao.findById(id);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        Review review = reviewDao.findByOrderId(id);
        request.setAttribute("order", order);
        request.setAttribute("review", review);
        request.getRequestDispatcher("/chen_zi_hao/orderDetail.jsp").forward(request, response);
    }
}
