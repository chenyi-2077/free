package com.freelite.dao;

import com.freelite.model.Delivery;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDao {

    public int insert(Delivery d) {
        String sql = "INSERT INTO delivery (order_id, project_id, user_id, title, description, file_name, file_path, file_size, file_type) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (d.getOrderId() > 0) {
                ps.setInt(1, d.getOrderId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, d.getProjectId());
            ps.setInt(3, d.getUserId());
            ps.setString(4, d.getTitle());
            ps.setString(5, d.getDescription());
            ps.setString(6, d.getFileName());
            ps.setString(7, d.getFilePath());
            ps.setLong(8, d.getFileSize());
            ps.setString(9, d.getFileType());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Delivery> findByProjectId(int projectId) {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT d.*, u.display_name AS user_name FROM delivery d "
                + "LEFT JOIN user u ON d.user_id = u.id "
                + "WHERE d.project_id=? ORDER BY d.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDelivery(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Delivery> findByOrderId(int orderId) {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT d.*, u.display_name AS user_name FROM delivery d "
                + "LEFT JOIN user u ON d.user_id = u.id "
                + "WHERE d.order_id=? ORDER BY d.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDelivery(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Delivery mapDelivery(ResultSet rs) throws SQLException {
        Delivery d = new Delivery();
        d.setId(rs.getInt("id"));
        d.setOrderId(rs.getInt("order_id"));
        d.setProjectId(rs.getInt("project_id"));
        d.setUserId(rs.getInt("user_id"));
        d.setTitle(rs.getString("title"));
        d.setDescription(rs.getString("description"));
        d.setFileName(rs.getString("file_name"));
        d.setFilePath(rs.getString("file_path"));
        d.setFileSize(rs.getLong("file_size"));
        d.setFileType(rs.getString("file_type"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) d.setCreatedAt(ts.toLocalDateTime());
        d.setUserName(rs.getString("user_name"));
        return d;
    }
}
