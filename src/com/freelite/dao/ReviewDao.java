package com.freelite.dao;

import com.freelite.model.Review;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    public Review findByOrderId(int orderId) {
        String sql = "SELECT * FROM review WHERE order_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapReview(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Review> findByUserId(int userId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE to_user_id=? ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapReview(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Review review) {
        String sql = "INSERT INTO review (order_id, from_user_id, to_user_id, score, comment) VALUES (?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, review.getOrderId());
            ps.setInt(2, review.getFromUserId());
            ps.setInt(3, review.getToUserId());
            ps.setInt(4, review.getScore());
            ps.setString(5, review.getComment());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Review mapReview(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getInt("id"));
        r.setOrderId(rs.getInt("order_id"));
        r.setFromUserId(rs.getInt("from_user_id"));
        r.setToUserId(rs.getInt("to_user_id"));
        r.setScore(rs.getInt("score"));
        r.setComment(rs.getString("comment"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) r.setCreatedAt(ts.toLocalDateTime());
        return r;
    }
}
