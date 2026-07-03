package com.freelite.dao;

import com.freelite.model.ProjectMessage;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectMessageDao {

    public List<ProjectMessage> findByProjectId(int projectId) {
        List<ProjectMessage> list = new ArrayList<>();
        String sql = "SELECT m.*, u.display_name as senderName " +
                     "FROM project_message m LEFT JOIN user u ON m.sender_id = u.id " +
                     "WHERE m.project_id = ? ORDER BY m.created_at ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(ProjectMessage message) {
        String sql = "INSERT INTO project_message (project_id, sender_id, content, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getProjectId());
            ps.setInt(2, message.getSenderId());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private ProjectMessage mapMessage(ResultSet rs) throws SQLException {
        ProjectMessage m = new ProjectMessage();
        m.setId(rs.getInt("id"));
        m.setProjectId(rs.getInt("project_id"));
        m.setSenderId(rs.getInt("sender_id"));
        m.setContent(rs.getString("content"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setCreatedAt(ts.toLocalDateTime());
        m.setSenderName(rs.getString("senderName"));
        return m;
    }
}
