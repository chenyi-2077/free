package chen_zi_hao;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ReviewDao reviewDao = new ReviewDao();
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        int orderId = Integer.parseInt(orderIdStr);
        Order order = orderDao.findById(orderId);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        request.setAttribute("order", order);
        request.getRequestDispatcher("/chen_zi_hao/review.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String scoreStr = request.getParameter("score");
        String comment = request.getParameter("comment");

        if (orderIdStr == null || scoreStr == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);
        int score = Integer.parseInt(scoreStr);
        if (comment == null) comment = "";

        Order order = orderDao.findById(orderId);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        // 独立版本使用固定用户 ID 模拟（雇主评价自由职业者）
        int fromUserId = 1;   // 模拟当前登录用户
        int toUserId = order.getFreelancerId();

        Review review = new Review();
        review.setOrderId(orderId);
        review.setFromUserId(fromUserId);
        review.setToUserId(toUserId);
        review.setScore(score);
        review.setComment(comment);

        reviewDao.insert(review);

        response.sendRedirect(request.getContextPath() + "/order/detail?id=" + orderId);
    }
}
