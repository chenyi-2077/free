package chen_yi_an;
import chen_kai_bo.Project;
import chen_kai_bo.Bid;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Category;
import chen_xi_rui.BidDao;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

import chen_yi_an.User;
import chen_yi_an.EscrowService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class RechargeServlet extends HttpServlet {
    private EscrowService escrowService = new EscrowService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String amountStr = req.getParameter("amount");
        if (amountStr == null || amountStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/wallet");
        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
        escrowService.recharge(loginUser.getId(), amount, "在线充值 ¥" + String.format("%.2f", amount));
        req.getSession().setAttribute("successMsg", "✅ 充值成功！¥" + String.format("%.2f", amount) + " 已到账。");
        resp.sendRedirect(req.getContextPath() + "/wallet");
    }
}
