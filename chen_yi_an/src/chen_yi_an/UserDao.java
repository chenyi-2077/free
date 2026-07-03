package chen_yi_an;

import com.freelite.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDao {

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    public int insert(User user) {
        String sql = "INSERT INTO users (email, password, role, display_name, skills) VALUES (?, ?, ?, ?, ?)";
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
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                user.setId(id);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return 0;
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET display_name = ?, skills = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getDisplayName());
            ps.setString(2, user.getSkills());
            ps.setInt(3, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setDisplayName(rs.getString("display_name"));
        user.setAvatar(rs.getString("avatar"));
        user.setSkills(rs.getString("skills"));
        user.setRating(rs.getDouble("rating"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            user.setCreatedAt(ts.toLocalDateTime());
        }
        return user;
    }
}
