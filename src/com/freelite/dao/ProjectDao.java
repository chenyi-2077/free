package com.freelite.dao;

import com.freelite.model.Project;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    /**
     * 分页查询项目列表（支持关键字 + 分类筛选）
     */
    public List<Project> search(String keyword, int categoryId, int page, int pageSize) {
        List<Project> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "LEFT JOIN user u ON p.employer_id = u.id "
                + "WHERE 1=1 ");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (p.title LIKE ? OR p.description LIKE ?)");
        }
        if (categoryId > 0) {
            sql.append(" AND p.category_id = ?");
        }
        sql.append(" ORDER BY p.created_at DESC LIMIT ? OFFSET ?");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (categoryId > 0) {
                ps.setInt(idx++, categoryId);
            }
            ps.setInt(idx++, pageSize);
            ps.setInt(idx++, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapProject(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int count(String keyword, int categoryId) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM project p WHERE 1=1 ");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (p.title LIKE ? OR p.description LIKE ?)");
        }
        if (categoryId > 0) {
            sql.append(" AND p.category_id = ?");
        }
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (categoryId > 0) {
                ps.setInt(idx++, categoryId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Project findById(int id) {
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "LEFT JOIN user u ON p.employer_id = u.id "
                + "WHERE p.id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapProject(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(Project p) {
        String sql = "INSERT INTO project (title, description, budget, deadline, category_id, employer_id, status) "
                + "VALUES (?,?,?,?,?,?,'open')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getTitle());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getBudget());
            if (p.getDeadline() != null) {
                ps.setDate(4, Date.valueOf(p.getDeadline()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setInt(5, p.getCategoryId());
            ps.setInt(6, p.getEmployerId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE project SET status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Project mapProject(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setBudget(rs.getDouble("budget"));
        Date dd = rs.getDate("deadline");
        if (dd != null) p.setDeadline(dd.toLocalDate());
        p.setCategoryId(rs.getInt("category_id"));
        p.setEmployerId(rs.getInt("employer_id"));
        p.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) p.setCreatedAt(ts.toLocalDateTime());
        p.setCategoryName(rs.getString("category_name"));
        p.setEmployerName(rs.getString("employer_name"));
        return p;
    }
}
