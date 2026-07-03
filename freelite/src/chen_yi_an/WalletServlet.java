package chen_yi_an;

import chen_yi_an.WalletDao;
import chen_yi_an.TransactionLog;
import chen_yi_an.User;
import chen_yi_an.Wallet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/wallet")
public class WalletServlet extends HttpServlet {

    private WalletDao walletDao = new WalletDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Wallet wallet = walletDao.getOrCreate(user.getId());
        List<TransactionLog> logs = walletDao.findTransactionLogs(user.getId());

        req.setAttribute("wallet", wallet);
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/A-user/wallet.jsp").forward(req, resp);
    }
}
