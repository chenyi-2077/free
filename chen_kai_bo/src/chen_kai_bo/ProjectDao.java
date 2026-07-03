package chen_kai_bo;

import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    public List<Project> findAll() {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "LEFT JOIN user u ON p.employer_id = u.id "
                + "ORDER BY p.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProject(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    public Project findById(int id) {
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "LEFT JOIN user u ON p.employer_id = u.id "
                + "WHERE p.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapProject(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    public List<Project> findByEmployerId(int employerId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name, u.display_name AS employer_name "
                + "FROM project p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "LEFT JOIN user u ON p.employer_id = u.id "
                + "WHERE p.employer_id = ? "
                + "ORDER BY p.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, employerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProject(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    public int insert(Project project) {
        String sql = "INSERT INTO project (title, description, budget, deadline, category_id, employer_id, status, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setDouble(3, project.getBudget());
            ps.setDate(4, project.getDeadline() != null ? Date.valueOf(project.getDeadline()) : null);
            ps.setInt(5, project.getCategoryId());
            ps.setInt(6, project.getEmployerId());
            ps.setString(7, project.getStatus() != null ? project.getStatus() : "open");
            ps.setObject(8, project.getCreatedAt() != null ? project.getCreatedAt() : LocalDateTime.now());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return -1;
    }

    public boolean update(Project project) {
        String sql = "UPDATE project SET title = ?, description = ?, budget = ?, deadline = ?, "
                + "category_id = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setDouble(3, project.getBudget());
            ps.setDate(4, project.getDeadline() != null ? Date.valueOf(project.getDeadline()) : null);
            ps.setInt(5, project.getCategoryId());
            ps.setString(6, project.getStatus());
            ps.setInt(7, project.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public List<Project> search(String keyword, Integer categoryId) {
        List<Project> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.*, c.name AS category_name, u.display_name AS employer_name ")
           .append("FROM project p ")
           .append("LEFT JOIN category c ON p.category_id = c.id ")
           .append("LEFT JOIN user u ON p.employer_id = u.id ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.title LIKE ? OR p.description LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        sql.append("ORDER BY p.created_at DESC");

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
            while (rs.next()) {
                list.add(mapProject(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    private Project mapProject(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setBudget(rs.getDouble("budget"));

        Date deadlineDate = rs.getDate("deadline");
        if (deadlineDate != null) {
            p.setDeadline(deadlineDate.toLocalDate());
        }

        p.setCategoryId(rs.getInt("category_id"));
        p.setEmployerId(rs.getInt("employer_id"));
        p.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            p.setCreatedAt(ts.toLocalDateTime());
        }

        p.setCategoryName(rs.getString("category_name"));
        p.setEmployerName(rs.getString("employer_name"));

        return p;
    }
}
