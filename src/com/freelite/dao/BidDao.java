package com.freelite.dao;

import com.freelite.model.Bid;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BidDao {

    public List<Bid> findByProjectId(int projectId) {
        List<Bid> list = new ArrayList<>();
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating "
                + "FROM bid b LEFT JOIN user u ON b.freelancer_id = u.id "
                + "WHERE b.project_id=? ORDER BY b.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapBid(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Bid> findByFreelancerId(int freelancerId) {
        List<Bid> list = new ArrayList<>();
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating "
                + "FROM bid b LEFT JOIN user u ON b.freelancer_id = u.id "
                + "WHERE b.freelancer_id=? ORDER BY b.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, freelancerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapBid(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Bid findById(int id) {
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating "
                + "FROM bid b LEFT JOIN user u ON b.freelancer_id = u.id WHERE b.id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapBid(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(Bid bid) {
        String sql = "INSERT INTO bid (project_id, freelancer_id, amount, days, proposal, status) "
                + "VALUES (?,?,?,?,?,'pending')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bid.getProjectId());
            ps.setInt(2, bid.getFreelancerId());
            ps.setDouble(3, bid.getAmount());
            ps.setInt(4, bid.getDays());
            ps.setString(5, bid.getProposal());
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
        String sql = "UPDATE bid SET status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bid mapBid(ResultSet rs) throws SQLException {
        Bid b = new Bid();
        b.setId(rs.getInt("id"));
        b.setProjectId(rs.getInt("project_id"));
        b.setFreelancerId(rs.getInt("freelancer_id"));
        b.setAmount(rs.getDouble("amount"));
        b.setDays(rs.getInt("days"));
        b.setProposal(rs.getString("proposal"));
        b.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) b.setCreatedAt(ts.toLocalDateTime());
        b.setFreelancerName(rs.getString("freelancer_name"));
        b.setFreelancerRating(rs.getDouble("freelancer_rating"));
        return b;
    }
}
