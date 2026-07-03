package chen_zi_hao;

import chen_zi_hao.OrderDao;
import chen_zi_hao.ReviewDao;
import chen_yi_an.UserDao;
import chen_zi_hao.Order;
import chen_zi_hao.Review;
import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private ReviewDao reviewDao = new ReviewDao();
    private OrderDao orderDao = new OrderDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDao.findById(Integer.parseInt(orderIdStr));
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        req.setAttribute("order", order);
        req.getRequestDispatcher("/D-order/review.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        String scoreStr = req.getParameter("score");
        String comment = req.getParameter("comment");

        if (orderIdStr == null || scoreStr == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDao.findById(Integer.parseInt(orderIdStr));
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // Determine who gets reviewed
        int toUserId;
        if (order.getEmployerId() == user.getId()) {
            toUserId = order.getFreelancerId();
        } else {
            toUserId = order.getEmployerId();
        }

        Review review = new Review();
        review.setOrderId(order.getId());
        review.setFromUserId(user.getId());
        review.setToUserId(toUserId);
        try {
            review.setScore(Integer.parseInt(scoreStr));
        } catch (NumberFormatException e) {
            review.setScore(5);
        }
        review.setComment(comment);

        reviewDao.insert(review);

        // Update user rating
        List<Review> reviews = reviewDao.findByToUserId(toUserId);
        double avg = reviews.stream().mapToInt(Review::getScore).average().orElse(0);
        User targetUser = userDao.findById(toUserId);
        if (targetUser != null) {
            targetUser.setRating(Math.round(avg * 10) / 10.0);
            userDao.update(targetUser);
        }

        resp.sendRedirect(req.getContextPath() + "/order/detail?id=" + orderIdStr);
    }
}
