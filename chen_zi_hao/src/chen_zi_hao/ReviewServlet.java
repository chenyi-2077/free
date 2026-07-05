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
        // 获取登录用户
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIdStr = request.getParameter("orderId");
        String scoreStr = request.getParameter("score");
        String comment = request.getParameter("comment");

        if (orderIdStr == null || scoreStr == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);
        int score = Integer.parseInt(scoreStr);

        // 评分校验
        if (score < 1 || score > 5) {
            request.getSession().setAttribute("errorMsg", "评分必须在 1-5 之间");
            response.sendRedirect(request.getContextPath() + "/order/detail?id=" + orderId);
            return;
        }
        if (comment == null) comment = "";

        Order order = orderDao.findById(orderId);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        // 防重复评价
        if (reviewDao.findByOrderId(orderId) != null) {
            request.getSession().setAttribute("errorMsg", "该订单已评价，不可重复提交");
            response.sendRedirect(request.getContextPath() + "/order/detail?id=" + orderId);
            return;
        }

        // 确定评价对象
        int toUserId;
        if (loginUser.getId() == order.getEmployerId()) {
            toUserId = order.getFreelancerId();
        } else {
            toUserId = order.getEmployerId();
        }

        Review review = new Review();
        review.setOrderId(orderId);
        review.setFromUserId(loginUser.getId());
        review.setToUserId(toUserId);
        review.setScore(score);
        review.setComment(comment);

        reviewDao.insert(review);

        response.sendRedirect(request.getContextPath() + "/order/detail?id=" + orderId);
    }
}
