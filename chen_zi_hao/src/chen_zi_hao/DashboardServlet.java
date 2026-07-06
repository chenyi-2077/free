package chen_zi_hao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 独立版本：session有user就用该用户的数据，无则显示全站统计数据
        HttpSession session = req.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        int totalOrders, completedOrders, inProgressOrders;
        List<Order> recentOrders;

        if (user != null) {
            int userId = user.getId();
            totalOrders = orderDao.countByUserId(userId);
            completedOrders = orderDao.countByUserId(userId, "completed");
            inProgressOrders = orderDao.countByUserId(userId, "in_progress");
            recentOrders = orderDao.findRecentByUserId(userId, 5);
        } else {
            // 未登录：显示全站统计数据
            java.util.Map<String, Integer> stats = orderDao.getDashboardStats();
            totalOrders = stats.getOrDefault("orderCount", 0);
            completedOrders = orderDao.countAllByStatus("completed");
            inProgressOrders = orderDao.countAllByStatus("in_progress");
            recentOrders = orderDao.findRecentAll(5);
        }

        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("completedOrders", completedOrders);
        req.setAttribute("inProgressOrders", inProgressOrders);
        req.setAttribute("recentOrders", recentOrders);
        req.getRequestDispatcher("/chen_zi_hao/dashboard.jsp").forward(req, resp);
    }
}
