package com.freelite.dao;

import com.freelite.model.Delivery;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDao {

    public List<Delivery> findByProjectId(int projectId) {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT d.*, u.display_name as senderName " +
                     "FROM delivery d LEFT JOIN user u ON d.sender_id = u.id " +
                     "WHERE d.project_id = ? ORDER BY d.created_at ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDelivery(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Delivery delivery) {
        String sql = "INSERT INTO delivery (project_id, sender_id, content, file_name, file_path, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, delivery.getProjectId());
            ps.setInt(2, delivery.getSenderId());
            ps.setString(3, delivery.getContent());
            ps.setString(4, delivery.getFileName());
            ps.setString(5, delivery.getFilePath());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Delivery mapDelivery(ResultSet rs) throws SQLException {
        Delivery d = new Delivery();
        d.setId(rs.getInt("id"));
        d.setProjectId(rs.getInt("project_id"));
        d.setSenderId(rs.getInt("sender_id"));
        d.setContent(rs.getString("content"));
        d.setFileName(rs.getString("file_name"));
        d.setFilePath(rs.getString("file_path"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) d.setCreatedAt(ts.toLocalDateTime());
        d.setSenderName(rs.getString("senderName"));
        return d;
    }
}
