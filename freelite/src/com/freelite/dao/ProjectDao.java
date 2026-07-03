package com.freelite.dao;

import com.freelite.model.Project;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    public List<Project> findAll() {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name as categoryName, u.display_name as employerName " +
                     "FROM project p LEFT JOIN category c ON p.category_id = c.id " +
                     "LEFT JOIN user u ON p.employer_id = u.id ORDER BY p.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Project findById(int id) {
        String sql = "SELECT p.*, c.name as categoryName, u.display_name as employerName " +
                     "FROM project p LEFT JOIN category c ON p.category_id = c.id " +
                     "LEFT JOIN user u ON p.employer_id = u.id WHERE p.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProject(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> findByEmployerId(int employerId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name as categoryName, u.display_name as employerName " +
                     "FROM project p LEFT JOIN category c ON p.category_id = c.id " +
                     "LEFT JOIN user u ON p.employer_id = u.id WHERE p.employer_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProject(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Project> findByFreelancerId(int freelancerId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name as categoryName, u.display_name as employerName " +
                     "FROM project p LEFT JOIN category c ON p.category_id = c.id " +
                     "LEFT JOIN user u ON p.employer_id = u.id " +
                     "WHERE p.id IN (SELECT project_id FROM bid WHERE freelancer_id = ?) " +
                     "ORDER BY p.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, freelancerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProject(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Project project) {
        String sql = "INSERT INTO project (title, description, budget, deadline, category_id, employer_id, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setDouble(3, project.getBudget());
            ps.setDate(4, project.getDeadline() != null ? Date.valueOf(project.getDeadline()) : null);
            ps.setInt(5, project.getCategoryId());
            ps.setInt(6, project.getEmployerId());
            ps.setString(7, "open");
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update(Project project) {
        String sql = "UPDATE project SET title = ?, description = ?, budget = ?, deadline = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setDouble(3, project.getBudget());
            ps.setDate(4, project.getDeadline() != null ? Date.valueOf(project.getDeadline()) : null);
            ps.setInt(5, project.getCategoryId());
            ps.setInt(6, project.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE project SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Project> search(String keyword, Integer categoryId) {
        List<Project> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.*, c.name as categoryName, u.display_name as employerName " +
            "FROM project p LEFT JOIN category c ON p.category_id = c.id " +
            "LEFT JOIN user u ON p.employer_id = u.id WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (p.title LIKE ? OR p.description LIKE ?)");
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id = ?");
        }
        sql.append(" ORDER BY p.created_at DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
            }
            if (categoryId != null && categoryId > 0) {
                ps.setInt(paramIndex, categoryId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProject(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Project mapProject(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setBudget(rs.getDouble("budget"));
        Date d = rs.getDate("deadline");
        if (d != null) p.setDeadline(d.toLocalDate());
        p.setCategoryId(rs.getInt("category_id"));
        p.setEmployerId(rs.getInt("employer_id"));
        p.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) p.setCreatedAt(ts.toLocalDateTime());
        p.setCategoryName(rs.getString("categoryName"));
        p.setEmployerName(rs.getString("employerName"));
        return p;
    }
}
