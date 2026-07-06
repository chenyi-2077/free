package com.freelite.dao;

import com.freelite.util.DBUtil;
import java.sql.*;

/**
 * 评价数据访问层
 * D负责
 */
public class ReviewDAO {

    /**
     * 提交评价
     */
    public boolean insert(int orderId, int fromUserId, int toUserId, int score, String comment) {
        String sql = "INSERT INTO review (order_id, from_user_id, to_user_id, score, comment) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, fromUserId);
            ps.setInt(3, toUserId);
            ps.setInt(4, score);
            ps.setString(5, comment);

            boolean ok = ps.executeUpdate() > 0;

            // 更新被评价人的评分
            if (ok) {
                PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE user u SET u.rating = (SELECT AVG(r.score) FROM review r WHERE r.to_user_id = ?) WHERE u.id = ?");
                ps2.setInt(1, toUserId);
                ps2.setInt(2, toUserId);
                ps2.executeUpdate();
                ps2.close();
            }

            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
    }

    /**
     * 检查该订单是否已被评价
     */
    public boolean hasReviewed(int orderId) {
        String sql = "SELECT COUNT(*) FROM review WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return false;
    }
}
