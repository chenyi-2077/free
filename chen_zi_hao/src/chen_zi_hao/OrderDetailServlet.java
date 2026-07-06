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
        javax.servlet.http.HttpSession session = request.getSession(false);
        User loginUser = (User) (session != null ? session.getAttribute("user") : null);

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
        request.setAttribute("order", order);

        Review review = reviewDao.findByOrderId(id);
        // 独立版本：直接设单个review，JSP中用List包装便于兼容
        java.util.List<Review> reviewList = review != null ? java.util.Collections.singletonList(review) : new java.util.ArrayList<>();
        request.setAttribute("reviews", reviewList);
        request.setAttribute("isEmployer", loginUser != null && loginUser.getId() == order.getEmployerId());
        request.setAttribute("isFreelancer", loginUser != null && loginUser.getId() == order.getFreelancerId());

        request.getRequestDispatcher("/chen_zi_hao/orderDetail.jsp").forward(request, response);
    }
}
