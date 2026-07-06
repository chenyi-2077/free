package com.freelite.dao;

import com.freelite.model.ProjectMessage;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMessageDao {

    public int insert(ProjectMessage msg) {
        String sql = "INSERT INTO project_message (project_id, sender_id, content) VALUES (?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, msg.getProjectId());
            ps.setInt(2, msg.getSenderId());
            ps.setString(3, msg.getContent());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<ProjectMessage> findByProjectId(int projectId) {
        List<ProjectMessage> list = new ArrayList<>();
        String sql = "SELECT m.*, u.display_name AS sender_name FROM project_message m "
                + "LEFT JOIN user u ON m.sender_id = u.id "
                + "WHERE m.project_id=? ORDER BY m.created_at ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapMessage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ProjectMessage mapMessage(ResultSet rs) throws SQLException {
        ProjectMessage m = new ProjectMessage();
        m.setId(rs.getInt("id"));
        m.setProjectId(rs.getInt("project_id"));
        m.setSenderId(rs.getInt("sender_id"));
        m.setContent(rs.getString("content"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setCreatedAt(ts.toLocalDateTime());
        m.setSenderName(rs.getString("sender_name"));
        return m;
    }
}
