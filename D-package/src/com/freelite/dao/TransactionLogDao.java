package com.freelite.dao;

import com.freelite.model.TransactionLog;
import com.freelite.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogDao {

    public void insert(TransactionLog log) {
        String sql = "INSERT INTO transaction_log (user_id, type, amount, balance_before, balance_after, "
                + "frozen_before, frozen_after, order_id, description) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getUserId());
            ps.setString(2, log.getType());
            ps.setDouble(3, log.getAmount());
            ps.setDouble(4, log.getBalanceBefore());
            ps.setDouble(5, log.getBalanceAfter());
            ps.setDouble(6, log.getFrozenBefore());
            ps.setDouble(7, log.getFrozenAfter());
            if (log.getOrderId() != null) {
                ps.setInt(8, log.getOrderId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setString(9, log.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TransactionLog> findByUserId(int userId) {
        List<TransactionLog> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction_log WHERE user_id=? ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapLog(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private TransactionLog mapLog(ResultSet rs) throws SQLException {
        TransactionLog log = new TransactionLog();
        log.setId(rs.getInt("id"));
        log.setUserId(rs.getInt("user_id"));
        log.setType(rs.getString("type"));
        log.setAmount(rs.getDouble("amount"));
        log.setBalanceBefore(rs.getDouble("balance_before"));
        log.setBalanceAfter(rs.getDouble("balance_after"));
        log.setFrozenBefore(rs.getDouble("frozen_before"));
        log.setFrozenAfter(rs.getDouble("frozen_after"));
        int oid = rs.getInt("order_id");
        if (!rs.wasNull()) log.setOrderId(oid);
        log.setDescription(rs.getString("description"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) log.setCreatedAt(ts.toLocalDateTime());
        return log;
    }
}
