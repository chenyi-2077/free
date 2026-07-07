package chen_xi_rui;

import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BidDao {

    /**
     * 根据项目ID查询所有竞标（携带自由职业者姓名和评分）
     */
    public List<Bid> findByProjectId(int projectId) {
        List<Bid> list = new ArrayList<>();
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating " +
                     "FROM bid b " +
                     "LEFT JOIN user u ON b.freelancer_id = u.id " +
                     "WHERE b.project_id = ? " +
                     "ORDER BY b.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据自由职业者ID查询其所有竞标
     */
    public List<Bid> findByFreelancerId(int freelancerId) {
        List<Bid> list = new ArrayList<>();
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating " +
                     "FROM bid b " +
                     "LEFT JOIN user u ON b.freelancer_id = u.id " +
                     "WHERE b.freelancer_id = ? " +
                     "ORDER BY b.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, freelancerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据ID查询单个竞标
     */
    public Bid findById(int id) {
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating " +
                     "FROM bid b " +
                     "LEFT JOIN user u ON b.freelancer_id = u.id " +
                     "WHERE b.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入新竞标
     */
    public int insert(Bid bid) {
        String sql = "INSERT INTO bid (project_id, freelancer_id, amount, days, proposal, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bid.getProjectId());
            ps.setInt(2, bid.getFreelancerId());
            ps.setDouble(3, bid.getAmount());
            ps.setInt(4, bid.getDays());
            ps.setString(5, bid.getProposal());
            ps.setString(6, bid.getStatus() != null ? bid.getStatus() : "pending");
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
            return affected;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新竞标状态
     */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE bid SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将 ResultSet 当前行映射为 Bid 对象
     */
    private Bid mapRow(ResultSet rs) throws SQLException {
        Bid bid = new Bid();
        bid.setId(rs.getInt("id"));
        bid.setProjectId(rs.getInt("project_id"));
        bid.setFreelancerId(rs.getInt("freelancer_id"));
        bid.setAmount(rs.getDouble("amount"));
        bid.setDays(rs.getInt("days"));
        bid.setProposal(rs.getString("proposal"));
        bid.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            bid.setCreatedAt(ts.toLocalDateTime());
        }
        bid.setFreelancerName(rs.getString("freelancer_name"));
        bid.setFreelancerRating(rs.getDouble("freelancer_rating"));
        return bid;
    }

    public List<Bid> findAll() {
        List<Bid> list = new ArrayList<>();
        String sql = "SELECT * FROM bids ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

