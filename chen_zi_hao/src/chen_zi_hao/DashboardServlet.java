package chen_zi_hao;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Map<String, Integer> stats = orderDao.getDashboardStats();
        request.setAttribute("stats", stats);
        request.getRequestDispatcher("/chen_zi_hao/dashboard.jsp").forward(request, response);
    }
}
