package chen_yi_an;

import com.freelite.util.DBUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WalletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 独立版本：如果session有user就查钱包，无则显示空数据
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            request.setAttribute("balance", 0.0);
            request.setAttribute("frozen", 0.0);
            request.setAttribute("transactions", new ArrayList<>());
            request.setAttribute("info", "请先登录以查看钱包");
            request.getRequestDispatcher("/chen_yi_an/wallet.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();

            // Query wallet
            String walletSql = "SELECT balance, frozen FROM wallet WHERE user_id = ?";
            ps = conn.prepareStatement(walletSql);
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                request.setAttribute("balance", rs.getDouble("balance"));
                request.setAttribute("frozen", rs.getDouble("frozen"));
            } else {
                request.setAttribute("balance", 0.0);
                request.setAttribute("frozen", 0.0);
            }
            DBUtil.close(rs, ps);

            // Query transaction log
            String logSql = "SELECT id, amount, type, description, created_at FROM transaction_log WHERE user_id = ? ORDER BY created_at DESC";
            ps = conn.prepareStatement(logSql);
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            List<Map<String, Object>> transactions = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> txn = new java.util.HashMap<>();
                txn.put("id", rs.getInt("id"));
                txn.put("amount", rs.getDouble("amount"));
                txn.put("type", rs.getString("type"));
                txn.put("description", rs.getString("description"));
                Timestamp ts = rs.getTimestamp("created_at");
                txn.put("createdAt", ts != null ? ts.toLocalDateTime().toString() : "");
                transactions.add(txn);
            }
            request.setAttribute("transactions", transactions);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("balance", 0.0);
            request.setAttribute("frozen", 0.0);
            request.setAttribute("transactions", new ArrayList<>());
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        request.getRequestDispatcher("/chen_yi_an/wallet.jsp").forward(request, response);
    }
}
