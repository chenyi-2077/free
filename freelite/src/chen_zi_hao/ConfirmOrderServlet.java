package chen_zi_hao;
import chen_kai_bo.ProjectDao;
import chen_yi_an.EscrowService;
import chen_yi_an.UserDao;
import chen_yi_an.User;
import chen_kai_bo.Project;

import chen_zi_hao.OrderDao;
import chen_zi_hao.ProjectDao;
import chen_zi_hao.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 雇主确认完成 → 释放托管资金到自由职业者
 */
public class ConfirmOrderServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();
    private EscrowService escrowService = new EscrowService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String orderIdStr = req.getParameter("orderId");
        int orderId = Integer.parseInt(orderIdStr);
        com.freelite.model.Order order = orderDao.findById(orderId);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        // 只有雇主可以确认完成
        if (loginUser.getId() != order.getEmployerId()) {
        // 只有在 awaiting_confirm 状态才能确认
        if (!"awaiting_confirm".equals(order.getStatus())) {
            resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
        // 释放托管资金
        double amount = order.getAmount();
        boolean released = escrowService.releaseToFreelancer(
                order.getProjectId(),
                order.getEmployerId(),
                order.getFreelancerId(),
                amount
        );
        if (!released) {
            req.getSession().setAttribute("errorMsg", "❌ 释放资金失败，请稍后重试。");
        // 更新订单和项目状态为已完成
        orderDao.updateStatus(orderId, "completed");
        req.getSession().setAttribute("successMsg",
                "✅ 确认完成！¥" + String.format("%.2f", amount) + " 已释放到自由职业者的钱包。");
        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
