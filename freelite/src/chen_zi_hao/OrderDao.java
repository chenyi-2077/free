package chen_zi_hao;

import com.freelite.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class OrderDao {
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
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapOrder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Order> findByProject(int projectId) {
        String sql = "SELECT o.*, p.title AS project_title, e.display_name AS employer_name, f.display_name AS freelancer_name "
                + "WHERE o.project_id=? ORDER BY o.created_at DESC";
            ps.setInt(1, projectId);
    public Order findById(int id) {
                + "WHERE o.id=?";
            ps.setInt(1, id);
                if (rs.next()) return mapOrder(rs);
        return null;
    public int insert(Order order) {
        String sql = "INSERT INTO task_order (project_id, employer_id, freelancer_id, amount, escrow_amount, status) "
                + "VALUES (?,?,?,?,?,'in_progress')";
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getProjectId());
            ps.setInt(2, order.getEmployerId());
            ps.setInt(3, order.getFreelancerId());
            ps.setDouble(4, order.getAmount());
            ps.setDouble(5, order.getAmount()); // escrow_amount = amount
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
        return -1;
    public void updateStatus(int id, String status) {
        String sql = "UPDATE task_order SET status=? WHERE id=?";
            ps.setString(1, status);
            ps.setInt(2, id);
    public int countByUserId(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM task_order WHERE (employer_id=? OR freelancer_id=?) AND status=?";
            ps.setString(3, status);
        return 0;
    public int countByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM task_order WHERE employer_id=? OR freelancer_id=?";
    /**
     * 最近 5 条订单
     */
    public List<Order> findRecentByUserId(int userId, int limit) {
                + "ORDER BY o.created_at DESC LIMIT ?";
            ps.setInt(3, limit);
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setProjectId(rs.getInt("project_id"));
        o.setEmployerId(rs.getInt("employer_id"));
        o.setFreelancerId(rs.getInt("freelancer_id"));
        o.setAmount(rs.getDouble("amount"));
        o.setEscrowAmount(rs.getDouble("escrow_amount"));
        o.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) o.setCreatedAt(ts.toLocalDateTime());
        o.setProjectTitle(rs.getString("project_title"));
        o.setEmployerName(rs.getString("employer_name"));
        o.setFreelancerName(rs.getString("freelancer_name"));
        return o;
}
