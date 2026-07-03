package chen_zi_hao;

import chen_zi_hao.OrderDao;
import chen_zi_hao.Order;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Project;
import chen_yi_an.UserDao;
import chen_yi_an.User;
import chen_xi_rui.BidDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private OrderDao orderDao = new OrderDao();
    private UserDao userDao = new UserDao();
    private BidDao bidDao = new BidDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        List<Project> allProjects = projectDao.findAll();
        List<Order> allOrders = orderDao.findAll();

        long totalProjects = allProjects.size();
        long openProjects = allProjects.stream().filter(p -> "open".equals(p.getStatus())).count();
        long inProgressProjects = allProjects.stream().filter(p -> "in_progress".equals(p.getStatus())).count();
        long completedOrders = allOrders.stream().filter(o -> "completed".equals(o.getStatus()) || "confirmed".equals(o.getStatus())).count();

        Map<String, Long> statusCounts = allProjects.stream()
            .collect(Collectors.groupingBy(Project::getStatus, Collectors.counting()));

        double totalRevenue = 0;
        List<Order> completedOrderList = allOrders.stream()
            .filter(o -> "confirmed".equals(o.getStatus())).collect(Collectors.toList());
        for (Order o : completedOrderList) {
            totalRevenue += o.getAmount();
        }

        req.setAttribute("totalProjects", totalProjects);
        req.setAttribute("openProjects", openProjects);
        req.setAttribute("inProgressProjects", inProgressProjects);
        req.setAttribute("completedOrders", completedOrders);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("statusCounts", statusCounts);
        req.setAttribute("recentProjects", allProjects.stream().limit(5).collect(Collectors.toList()));

        req.getRequestDispatcher("/D-order/dashboard.jsp").forward(req, resp);
    }
}
