package chen_yi_an;

import chen_yi_an.WalletDao;
import chen_yi_an.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recharge")
public class RechargeServlet extends HttpServlet {

    private WalletDao walletDao = new WalletDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String amountStr = req.getParameter("amount");
        double amount = 0;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/wallet?error=invalid");
            return;
        }

        walletDao.recharge(user.getId(), amount);
        resp.sendRedirect(req.getContextPath() + "/wallet?success=true");
    }
}
