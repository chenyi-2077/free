package chen_yi_an;

import com.freelite.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDao {

    /**
     * 查询所有订单（带关联信息）
     */
    public List<Order> findAll() {
        String sql = "SELECT o.*, p.title AS project_title, " +
                     "ue.display_name AS employer_name, uf.display_name AS freelancer_name " +
                     "FROM task_order o " +
                     "JOIN project p ON o.project_id = p.id " +
                     "JOIN user ue ON o.employer_id = ue.id " +
                     "JOIN user uf ON o.freelancer_id = uf.id " +
                     "ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, st, conn);
        }
        return list;
    }

    /**
     * 按 id 查询订单
     */
    public Order findById(int id) {
        String sql = "SELECT o.*, p.title AS project_title, " +
                     "ue.display_name AS employer_name, uf.display_name AS freelancer_name " +
                     "FROM task_order o " +
                     "JOIN project p ON o.project_id = p.id " +
                     "JOIN user ue ON o.employer_id = ue.id " +
                     "JOIN user uf ON o.freelancer_id = uf.id " +
                     "WHERE o.id = ?";
        Order order = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                order = mapOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return order;
    }

    /**
     * 按雇主 id 查询订单
     */
    public List<Order> findByEmployerId(int employerId) {
        String sql = "SELECT o.*, p.title AS project_title, " +
                     "ue.display_name AS employer_name, uf.display_name AS freelancer_name " +
                     "FROM task_order o " +
                     "JOIN project p ON o.project_id = p.id " +
                     "JOIN user ue ON o.employer_id = ue.id " +
                     "JOIN user uf ON o.freelancer_id = uf.id " +
                     "WHERE o.employer_id = ? ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, employerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 按自由职业者 id 查询订单
     */
    public List<Order> findByFreelancerId(int freelancerId) {
        String sql = "SELECT o.*, p.title AS project_title, " +
                     "ue.display_name AS employer_name, uf.display_name AS freelancer_name " +
                     "FROM task_order o " +
                     "JOIN project p ON o.project_id = p.id " +
                     "JOIN user ue ON o.employer_id = ue.id " +
                     "JOIN user uf ON o.freelancer_id = uf.id " +
                     "WHERE o.freelancer_id = ? ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, freelancerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 插入订单
     */
    public int insert(Order order) {
        String sql = "INSERT INTO task_order (project_id, employer_id, freelancer_id, amount, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int generatedId = -1;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getProjectId());
            ps.setInt(2, order.getEmployerId());
            ps.setInt(3, order.getFreelancerId());
            ps.setDouble(4, order.getAmount());
            ps.setString(5, order.getStatus() != null ? order.getStatus() : "in_progress");
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return generatedId;
    }

    /**
     * 更新订单状态
     */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE task_order SET status = ? WHERE id = ?";
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
            DBUtil.close(ps, conn);
        }
    }

    /**
     * 获取看板统计数据
     * 返回 Map: projectCount, bidCount, orderCount, reviewCount
     */
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            st = conn.createStatement();

            // 项目数
            rs = st.executeQuery("SELECT COUNT(*) FROM project");
            stats.put("projectCount", rs.next() ? rs.getInt(1) : 0);
            rs.close();

            // 竞标数
            rs = st.executeQuery("SELECT COUNT(*) FROM bid");
            stats.put("bidCount", rs.next() ? rs.getInt(1) : 0);
            rs.close();

            // 订单数
            rs = st.executeQuery("SELECT COUNT(*) FROM task_order");
            stats.put("orderCount", rs.next() ? rs.getInt(1) : 0);
            rs.close();

            // 评价数
            rs = st.executeQuery("SELECT COUNT(*) FROM review");
            stats.put("reviewCount", rs.next() ? rs.getInt(1) : 0);
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, st, conn);
        }
        return stats;
    }

    public List<Order> findByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, "
                + "p.title AS project_title, "
                + "e.display_name AS employer_name, "
                + "f.display_name AS freelancer_name "
                + "FROM task_order o "
                + "LEFT JOIN project p ON o.project_id = p.id "
                + "LEFT JOIN user e ON o.employer_id = e.id "
                + "LEFT JOIN user f ON o.freelancer_id = f.id "
                + "WHERE o.employer_id=? OR o.freelancer_id=? "
                + "ORDER BY o.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    public List<Order> findByProject(int projectId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, p.title AS project_title, e.display_name AS employer_name, f.display_name AS freelancer_name "
                + "FROM task_order o "
                + "LEFT JOIN project p ON o.project_id = p.id "
                + "LEFT JOIN user e ON o.employer_id = e.id "
                + "LEFT JOIN user f ON o.freelancer_id = f.id "
                + "WHERE o.project_id=? ORDER BY o.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    public int countByUserId(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM task_order WHERE (employer_id=? OR freelancer_id=?) AND status=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setString(3, status);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return 0;
    }

    public int countByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM task_order WHERE employer_id=? OR freelancer_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return 0;
    }

    public List<Order> findRecentByUserId(int userId, int limit) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, "
                + "p.title AS project_title, "
                + "e.display_name AS employer_name, "
                + "f.display_name AS freelancer_name "
                + "FROM task_order o "
                + "LEFT JOIN project p ON o.project_id = p.id "
                + "LEFT JOIN user e ON o.employer_id = e.id "
                + "LEFT JOIN user f ON o.freelancer_id = f.id "
                + "WHERE o.employer_id=? OR o.freelancer_id=? "
                + "ORDER BY o.created_at DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    // ---- 内部工具 ----

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setProjectId(rs.getInt("project_id"));
        o.setEmployerId(rs.getInt("employer_id"));
        o.setFreelancerId(rs.getInt("freelancer_id"));
        o.setAmount(rs.getDouble("amount"));
        o.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            o.setCreatedAt(ts.toLocalDateTime());
        }
        o.setProjectTitle(rs.getString("project_title"));
        o.setEmployerName(rs.getString("employer_name"));
        o.setFreelancerName(rs.getString("freelancer_name"));
        return o;
    }
}
