package com.freelite.dao;

import com.freelite.model.Order;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单数据访问层
 * D负责
 */
public class OrderDAO {

    /**
     * 按用户查订单列表（作为雇主或自由职业者）
     */
    public List<Order> findByUser(int userId) {
        String sql = "SELECT o.*, p.title AS project_title, "
                + "e.display_name AS employer_name, f.display_name AS freelancer_name "
                + "FROM task_order o "
                + "JOIN project p ON o.project_id = p.id "
                + "JOIN user e ON o.employer_id = e.id "
                + "JOIN user f ON o.freelancer_id = f.id "
                + "WHERE o.employer_id = ? OR o.freelancer_id = ? "
                + "ORDER BY o.created_at DESC";
        return queryList(sql, userId, userId);
    }

    /**
     * 按ID查订单
     */
    public Order findById(int id) {
        String sql = "SELECT o.*, p.title AS project_title, "
                + "e.display_name AS employer_name, f.display_name AS freelancer_name "
                + "FROM task_order o "
                + "JOIN project p ON o.project_id = p.id "
                + "JOIN user e ON o.employer_id = e.id "
                + "JOIN user f ON o.freelancer_id = f.id "
                + "WHERE o.id = ?";
        List<Order> list = queryList(sql, id);
        return list.isEmpty() ? null : list.get(0);
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
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
    }

    /**
     * 查询所有订单（看板用）
     */
    public List<Order> findAll() {
        String sql = "SELECT o.*, p.title AS project_title, "
                + "e.display_name AS employer_name, f.display_name AS freelancer_name "
                + "FROM task_order o "
                + "JOIN project p ON o.project_id = p.id "
                + "JOIN user e ON o.employer_id = e.id "
                + "JOIN user f ON o.freelancer_id = f.id "
                + "ORDER BY o.created_at DESC";
        return queryList(sql);
    }

    /**
     * 统计各状态订单数量（看板用）
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM task_order WHERE status = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
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

    private List<Order> queryList(String sql, Object... params) {
        List<Order> list = new ArrayList<>();
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
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setProjectId(rs.getInt("project_id"));
                o.setProjectTitle(rs.getString("project_title"));
                o.setEmployerId(rs.getInt("employer_id"));
                o.setEmployerName(rs.getString("employer_name"));
                o.setFreelancerId(rs.getInt("freelancer_id"));
                o.setFreelancerName(rs.getString("freelancer_name"));
                o.setAmount(rs.getDouble("amount"));
                o.setStatus(rs.getString("status"));
                o.setCreatedAt(rs.getString("created_at"));
                list.add(o);
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
