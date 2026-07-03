package chen_zi_hao;

import com.freelite.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    /**
     * 按订单 id 查询评价
     */
    public Review findByOrderId(int orderId) {
        String sql = "SELECT r.*, u.display_name AS from_user_name " +
                     "FROM review r " +
                     "JOIN user u ON r.from_user_id = u.id " +
                     "WHERE r.order_id = ?";
        Review review = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                review = mapReview(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return review;
    }

    /**
     * 按评价人 id 查询评价列表
     */
    public List<Review> findByUserId(int userId) {
        String sql = "SELECT r.*, u.display_name AS from_user_name " +
                     "FROM review r " +
                     "JOIN user u ON r.from_user_id = u.id " +
                     "WHERE r.from_user_id = ? ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 按被评价人 id 查询评价列表
     */
    public List<Review> findByToUserId(int toUserId) {
        String sql = "SELECT r.*, u.display_name AS from_user_name " +
                     "FROM review r " +
                     "JOIN user u ON r.from_user_id = u.id " +
                     "WHERE r.to_user_id = ? ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, toUserId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 插入评价
     */
    public int insert(Review review) {
        String sql = "INSERT INTO review (order_id, from_user_id, to_user_id, score, comment) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int generatedId = -1;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getOrderId());
            ps.setInt(2, review.getFromUserId());
            ps.setInt(3, review.getToUserId());
            ps.setInt(4, review.getScore());
            ps.setString(5, review.getComment());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return generatedId;
    }

    // ---- 内部工具 ----

    private Review mapReview(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getInt("id"));
        r.setOrderId(rs.getInt("order_id"));
        r.setFromUserId(rs.getInt("from_user_id"));
        r.setToUserId(rs.getInt("to_user_id"));
        r.setScore(rs.getInt("score"));
        r.setComment(rs.getString("comment"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            r.setCreatedAt(ts.toLocalDateTime());
        }
        r.setFromUserName(rs.getString("from_user_name"));
        return r;
    }
}
