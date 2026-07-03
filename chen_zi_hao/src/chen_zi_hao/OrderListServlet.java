package chen_zi_hao;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = orderDao.findAll();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/chen_zi_hao/orderList.jsp").forward(request, response);
    }
}
