package com.freelite.dao;

import com.freelite.model.Project;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目数据访问层
 * B负责
 */
public class ProjectDAO {

    /**
     * 发布项目
     */
    public int insert(Project project) {
        String sql = "INSERT INTO project (title, description, budget, deadline, category_id, employer_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setDouble(3, project.getBudget());
            ps.setString(4, project.getDeadline());
            ps.setInt(5, project.getCategoryId());
            ps.setInt(6, project.getEmployerId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return -1;
    }

    /**
     * 分页查询所有项目（含分类名、雇主名）
     */
    public List<Project> findAll(int page, int pageSize) {
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "JOIN category c ON p.category_id = c.id "
                + "JOIN user u ON p.employer_id = u.id "
                + "ORDER BY p.created_at DESC "
                + "LIMIT ? OFFSET ?";
        return queryList(sql, pageSize, (page - 1) * pageSize);
    }

    /**
     * 按关键词搜索+分页
     */
    public List<Project> search(String keyword, int categoryId, int page, int pageSize) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "JOIN category c ON p.category_id = c.id "
                + "JOIN user u ON p.employer_id = u.id "
                + "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.title LIKE ? OR p.description LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
        }
        if (categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        sql.append("ORDER BY p.created_at DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        return queryList(sql.toString(), params.toArray());
    }

    /**
     * 查询总数（分页用）
     */
    public int count(String keyword, int categoryId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM project p WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.title LIKE ? OR p.description LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw); params.add(kw);
        }
        if (categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return 0;
    }

    /**
     * 按ID查项目详情
     */
    public Project findById(int id) {
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "JOIN category c ON p.category_id = c.id "
                + "JOIN user u ON p.employer_id = u.id "
                + "WHERE p.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return extractProject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return null;
    }

    /**
     * 按雇主查项目
     */
    public List<Project> findByEmployer(int employerId) {
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "JOIN category c ON p.category_id = c.id "
                + "JOIN user u ON p.employer_id = u.id "
                + "WHERE p.employer_id = ? ORDER BY p.created_at DESC";
        return queryList(sql, employerId);
    }

    /**
     * 更新项目状态
     */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE project SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
    }

    // ====== 公共查询方法 ======

    private List<Project> queryList(String sql, Object... params) {
        List<Project> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    private Project extractProject(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setBudget(rs.getDouble("budget"));
        p.setDeadline(rs.getString("deadline"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setEmployerId(rs.getInt("employer_id"));
        p.setEmployerName(rs.getString("employer_name"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getString("created_at"));
        return p;
    }
}
