package chen_zi_hao;

import chen_zi_hao.Order;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, p.title as projectTitle, e.display_name as employerName, f.display_name as freelancerName " +
                     "FROM task_order o " +
                     "LEFT JOIN project p ON o.project_id = p.id " +
                     "LEFT JOIN user e ON o.employer_id = e.id " +
                     "LEFT JOIN user f ON o.freelancer_id = f.id " +
                     "ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Order findById(int id) {
        String sql = "SELECT o.*, p.title as projectTitle, e.display_name as employerName, f.display_name as freelancerName " +
                     "FROM task_order o " +
                     "LEFT JOIN project p ON o.project_id = p.id " +
                     "LEFT JOIN user e ON o.employer_id = e.id " +
                     "LEFT JOIN user f ON o.freelancer_id = f.id WHERE o.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> findByEmployerId(int employerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, p.title as projectTitle, e.display_name as employerName, f.display_name as freelancerName " +
                     "FROM task_order o " +
                     "LEFT JOIN project p ON o.project_id = p.id " +
                     "LEFT JOIN user e ON o.employer_id = e.id " +
                     "LEFT JOIN user f ON o.freelancer_id = f.id WHERE o.employer_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findByFreelancerId(int freelancerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, p.title as projectTitle, e.display_name as employerName, f.display_name as freelancerName " +
                     "FROM task_order o " +
                     "LEFT JOIN project p ON o.project_id = p.id " +
                     "LEFT JOIN user e ON o.employer_id = e.id " +
                     "LEFT JOIN user f ON o.freelancer_id = f.id WHERE o.freelancer_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, freelancerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Order order) {
        String sql = "INSERT INTO task_order (project_id, employer_id, freelancer_id, amount, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getProjectId());
            ps.setInt(2, order.getEmployerId());
            ps.setInt(3, order.getFreelancerId());
            ps.setDouble(4, order.getAmount());
            ps.setString(5, "in_progress");
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

    public void updateStatus(int id, String status) {
        String sql = "UPDATE task_order SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setProjectId(rs.getInt("project_id"));
        o.setEmployerId(rs.getInt("employer_id"));
        o.setFreelancerId(rs.getInt("freelancer_id"));
        o.setAmount(rs.getDouble("amount"));
        o.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) o.setCreatedAt(ts.toLocalDateTime());
        o.setProjectTitle(rs.getString("projectTitle"));
        o.setEmployerName(rs.getString("employerName"));
        o.setFreelancerName(rs.getString("freelancerName"));
        return o;
    }
}
