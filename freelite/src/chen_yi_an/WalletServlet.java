package chen_yi_an;

import chen_yi_an.TransactionLogDao;
import chen_yi_an.WalletDao;
import chen_yi_an.TransactionLog;
import chen_yi_an.User;
import chen_yi_an.Wallet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class WalletServlet extends HttpServlet {

    private WalletDao walletDao = new WalletDao();
    private TransactionLogDao logDao = new TransactionLogDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Wallet wallet = walletDao.getOrCreate(loginUser.getId());
        List<TransactionLog> logs = logDao.findByUserId(loginUser.getId());

        req.setAttribute("wallet", wallet);
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/wallet.jsp").forward(req, resp);
    }
}
