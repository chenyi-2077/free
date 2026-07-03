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
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        int id = Integer.parseInt(idStr);
        orderDao.updateStatus(id, "completed");
        response.sendRedirect(request.getContextPath() + "/order/detail?id=" + id);
    }
}
