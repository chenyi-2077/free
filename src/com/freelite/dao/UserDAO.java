package com.freelite.dao;

import com.freelite.model.User;
import com.freelite.util.DBUtil;
import java.sql.*;

/**
 * 用户数据访问层
 * A负责
 */
public class UserDAO {

    /**
     * 根据邮箱查询用户（登录用）
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return null;
    }

    /**
     * 根据ID查询用户
     */
    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            DBUtil.closeConnection(conn);
        }
        return null;
    }

    /**
     * 注册新用户
     * @return 自增ID，失败返回 -1
     */
    public int register(User user) {
        String sql = "INSERT INTO user (email, password, role, display_name, skills) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getDisplayName());
            ps.setString(5, user.getSkills());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
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
     * 更新用户资料
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE user SET display_name=?, avatar=?, skills=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getDisplayName());
            ps.setString(2, user.getAvatar());
            ps.setString(3, user.getSkills());
            ps.setInt(4, user.getId());

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
     * 更新用户评分（项目完成后调用）
     */
    public boolean updateRating(int userId) {
        String sql = "UPDATE user u SET u.rating = ("
                + "  SELECT AVG(r.score) FROM review r WHERE r.to_user_id = ?"
                + ") WHERE u.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
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
     * 从 ResultSet 提取 User 对象
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setDisplayName(rs.getString("display_name"));
        user.setAvatar(rs.getString("avatar"));
        user.setSkills(rs.getString("skills"));
        user.setRating(rs.getDouble("rating"));
        user.setCreatedAt(rs.getString("created_at"));
        return user;
    }
}
