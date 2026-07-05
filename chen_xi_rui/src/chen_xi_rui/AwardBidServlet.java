package chen_xi_rui;

import com.freelite.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AwardBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取当前登录用户
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bidIdParam = request.getParameter("bidId");
        if (bidIdParam == null || bidIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        int bidId = Integer.parseInt(bidIdParam);
        Bid bid = bidDao.findById(bidId);
        if (bid == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        // 更新竞标状态为 accepted，其他竞标设为 rejected
        bidDao.updateStatus(bidId, "accepted");
        for (Bid other : bidDao.findByProjectId(bid.getProjectId())) {
            if (other.getId() != bidId && "pending".equals(other.getStatus())) {
                bidDao.updateStatus(other.getId(), "rejected");
            }
        }

        // 创建 order，使用真实雇主ID（而非硬编码1）
        createOrder(bid, loginUser.getId());

        response.sendRedirect(request.getContextPath() + "/bids?projectId=" + bid.getProjectId());
    }

    private void createOrder(Bid bid, int employerId) {
        String sql = "INSERT INTO task_order (project_id, freelancer_id, employer_id, amount, status, created_at) " +
                     "VALUES (?, ?, ?, ?, 'in_progress', ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bid.getProjectId());
            ps.setInt(2, bid.getFreelancerId());
            ps.setInt(3, employerId);
            ps.setDouble(4, bid.getAmount());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
