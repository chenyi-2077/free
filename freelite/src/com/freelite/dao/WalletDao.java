package com.freelite.dao;

import com.freelite.model.Wallet;
import com.freelite.util.DBUtil;

import java.sql.*;

public class WalletDao {

    /**
     * 获取用户钱包，不存在则创建
     */
    public Wallet getOrCreate(int userId) {
        String sql = "SELECT * FROM wallet WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapWallet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建钱包
        String insert = "INSERT INTO wallet (user_id, balance, frozen) VALUES (?, 0, 0)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getOrCreate(userId);
    }

    /**
     * 充值
     */
    public boolean recharge(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ? WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 冻结金额（担保支付托管）
     */
    public boolean freeze(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance - ?, frozen = frozen + ? WHERE user_id=? AND balance >= ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setDouble(2, amount);
            ps.setInt(3, userId);
            ps.setDouble(4, amount);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放冻结金额（确认完成）
     */
    public boolean release(int userId, double amount) {
        String sql = "UPDATE wallet SET frozen = frozen - ? WHERE user_id=? AND frozen >= ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.setDouble(3, amount);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 退款（解冻并退回雇主）
     */
    public boolean refund(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ?, frozen = frozen - ? WHERE user_id=? AND frozen >= ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setDouble(2, amount);
            ps.setInt(3, userId);
            ps.setDouble(4, amount);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 收入入账（自由职业者收到款）
     */
    public boolean income(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ? WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Wallet mapWallet(ResultSet rs) throws SQLException {
        Wallet w = new Wallet();
        w.setId(rs.getInt("id"));
        w.setUserId(rs.getInt("user_id"));
        w.setBalance(rs.getDouble("balance"));
        w.setFrozen(rs.getDouble("frozen"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) w.setCreatedAt(ts.toLocalDateTime());
        ts = rs.getTimestamp("updated_at");
        if (ts != null) w.setUpdatedAt(ts.toLocalDateTime());
        return w;
    }
}
