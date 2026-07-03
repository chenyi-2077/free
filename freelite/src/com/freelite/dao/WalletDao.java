package com.freelite.dao;

import com.freelite.model.TransactionLog;
import com.freelite.model.Wallet;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WalletDao {

    public Wallet getOrCreate(int userId) {
        String sql = "SELECT * FROM wallet WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapWallet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean recharge(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ? WHERE user_id = ?";
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

    public boolean freeze(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance - ?, frozen = frozen + ? WHERE user_id = ? AND balance >= ?";
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

    public boolean release(int userId, double amount) {
        String sql = "UPDATE wallet SET frozen = frozen - ? WHERE user_id = ? AND frozen >= ?";
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

    public boolean refund(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ?, frozen = frozen - ? WHERE user_id = ? AND frozen >= ?";
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

    public boolean income(int userId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ? WHERE user_id = ?";
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

    public List<TransactionLog> findTransactionLogs(int userId) {
        List<TransactionLog> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction_log WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransactionLog tl = new TransactionLog();
                    tl.setId(rs.getInt("id"));
                    tl.setUserId(rs.getInt("user_id"));
                    tl.setAmount(rs.getDouble("amount"));
                    tl.setType(rs.getString("type"));
                    tl.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) tl.setCreatedAt(ts.toLocalDateTime());
                    list.add(tl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void logTransaction(int userId, double amount, String type, String description) {
        String sql = "INSERT INTO transaction_log (user_id, amount, type, description, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, amount);
            ps.setString(3, type);
            ps.setString(4, description);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
