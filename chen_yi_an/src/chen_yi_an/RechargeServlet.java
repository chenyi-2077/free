package chen_yi_an;

import com.freelite.util.DBUtil;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RechargeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String amountStr = request.getParameter("amount");
        double amount = 0;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "请输入有效的充值金额");
            request.getRequestDispatcher("/chen_yi_an/wallet.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Update wallet balance
            String updateSql = "UPDATE wallets SET balance = balance + ? WHERE user_id = ?";
            ps = conn.prepareStatement(updateSql);
            ps.setDouble(1, amount);
            ps.setInt(2, user.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                // Wallet doesn't exist, create one
                DBUtil.close(ps);
                String insertSql = "INSERT INTO wallets (user_id, balance, frozen) VALUES (?, ?, 0)";
                ps = conn.prepareStatement(insertSql);
                ps.setInt(1, user.getId());
                ps.setDouble(2, amount);
                ps.executeUpdate();
            }
            DBUtil.close(ps);

            // Record transaction log
            String logSql = "INSERT INTO transaction_log (user_id, amount, type, description) VALUES (?, ?, 'recharge', '充值')";
            ps = conn.prepareStatement(logSql);
            ps.setInt(1, user.getId());
            ps.setDouble(2, amount);
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            e.printStackTrace();
            request.setAttribute("error", "充值失败，请重试");
            request.getRequestDispatcher("/chen_yi_an/wallet.jsp").forward(request, response);
            return;
        } finally {
            DBUtil.close(ps, conn);
        }

        response.sendRedirect(request.getContextPath() + "/wallet");
    }
}
