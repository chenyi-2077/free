package com.freelite.dao;

import com.freelite.model.Bid;
import com.freelite.model.Order;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 竞标数据访问层
 * C负责
 */
public class BidDAO {

    /**
     * 投递竞标
     */
    public int insert(Bid bid) {
        String sql = "INSERT INTO bid (project_id, freelancer_id, amount, days, proposal) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bid.getProjectId());
            ps.setInt(2, bid.getFreelancerId());
            ps.setDouble(3, bid.getAmount());
            ps.setInt(4, bid.getDays());
            ps.setString(5, bid.getProposal());
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
     * 按项目查所有竞标
     */
    public List<Bid> findByProject(int projectId) {
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating "
                + "FROM bid b JOIN user u ON b.freelancer_id = u.id "
                + "WHERE b.project_id = ? ORDER BY b.created_at DESC";
        return queryList(sql, projectId);
    }

    /**
     * 按自由职业者查竞标
     */
    public List<Bid> findByFreelancer(int freelancerId) {
        String sql = "SELECT b.*, u.display_name AS freelancer_name, u.rating AS freelancer_rating "
                + "FROM bid b JOIN user u ON b.freelancer_id = u.id "
                + "WHERE b.freelancer_id = ? ORDER BY b.created_at DESC";
        return queryList(sql, freelancerId);
    }

    /**
     * 检查是否已投递过（防止重复竞标）
     */
    public boolean hasBid(int projectId, int freelancerId) {
        String sql = "SELECT COUNT(*) FROM bid WHERE project_id = ? AND freelancer_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ps.setInt(2, freelancerId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return false;
    }

    /**
     * 选标—同时创建订单
     * 使用事务保证一致性
     */
    public boolean awardBid(int bidId, int projectId, int employerId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 查这个竞标的信息
            String selectSql = "SELECT freelancer_id, amount FROM bid WHERE id = ?";
            ps = conn.prepareStatement(selectSql);
            ps.setInt(1, bidId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int freelancerId = rs.getInt("freelancer_id");
            double amount = rs.getDouble("amount");
            rs.close();
            ps.close();

            // 2. 更新该竞标为 accepted
            ps = conn.prepareStatement("UPDATE bid SET status = 'accepted' WHERE id = ?");
            ps.setInt(1, bidId);
            ps.executeUpdate();
            ps.close();

            // 3. 将该项目的其他竞标设为 rejected
            ps = conn.prepareStatement("UPDATE bid SET status = 'rejected' WHERE project_id = ? AND id != ? AND status = 'pending'");
            ps.setInt(1, projectId);
            ps.setInt(2, bidId);
            ps.executeUpdate();
            ps.close();

            // 4. 更新项目状态为 in_progress
            ps = conn.prepareStatement("UPDATE project SET status = 'in_progress' WHERE id = ?");
            ps.setInt(1, projectId);
            ps.executeUpdate();
            ps.close();

            // 5. 创建订单
            ps = conn.prepareStatement(
                "INSERT INTO task_order (project_id, employer_id, freelancer_id, amount, status) VALUES (?, ?, ?, ?, 'in_progress')");
            ps.setInt(1, projectId);
            ps.setInt(2, employerId);
            ps.setInt(3, freelancerId);
            ps.setDouble(4, amount);
            ps.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
    }

    private List<Bid> queryList(String sql, Object... params) {
        List<Bid> list = new ArrayList<>();
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
                Bid b = new Bid();
                b.setId(rs.getInt("id"));
                b.setProjectId(rs.getInt("project_id"));
                b.setFreelancerId(rs.getInt("freelancer_id"));
                b.setFreelancerName(rs.getString("freelancer_name"));
                b.setFreelancerRating(rs.getDouble("freelancer_rating"));
                b.setAmount(rs.getDouble("amount"));
                b.setDays(rs.getInt("days"));
                b.setProposal(rs.getString("proposal"));
                b.setStatus(rs.getString("status"));
                b.setCreatedAt(rs.getString("created_at"));
                list.add(b);
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
}
