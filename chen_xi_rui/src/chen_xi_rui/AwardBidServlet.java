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

/**
 * 雇主授标：将竞标状态改为 accepted，同时创建 order（task_order）
 */
public class AwardBidServlet extends HttpServlet {

    private BidDao bidDao = new BidDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        // 更新竞标状态为 accepted
        bidDao.updateStatus(bidId, "accepted");

        // 创建 order（task_order）
        createOrder(bid);

        // 重定向回竞标列表页
        response.sendRedirect(request.getContextPath() + "/bids?projectId=" + bid.getProjectId());
    }

    /**
     * 在 task_order 表中创建订单记录
     */
    private void createOrder(Bid bid) {
        String sql = "INSERT INTO task_order (project_id, freelancer_id, employer_id, bid_id, amount, days, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'pending', ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bid.getProjectId());
            ps.setInt(2, bid.getFreelancerId());
            ps.setInt(3, 1); // 默认雇主ID为1
            ps.setInt(4, bid.getId());
            ps.setDouble(5, bid.getAmount());
            ps.setInt(6, bid.getDays());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
