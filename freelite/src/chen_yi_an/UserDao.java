package chen_yi_an;

import com.freelite.util.DBUtil;
import java.sql.*;
public class UserDao {
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id=?";
            ps.setInt(1, id);
    public int insert(User user) {
        String sql = "INSERT INTO user (email, password, role, display_name, skills, rating) VALUES (?,?,?,?,?,0.0)";
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getDisplayName());
            ps.setString(5, user.getSkills());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
        return -1;
    public void updateRole(int userId, String role) {
        String sql = "UPDATE user SET role=? WHERE id=?";
            ps.setString(1, role);
            ps.setInt(2, userId);
    public void update(User user) {
        String sql = "UPDATE user SET display_name=?, skills=? WHERE id=?";
            ps.setString(1, user.getDisplayName());
            ps.setString(2, user.getSkills());
            ps.setInt(3, user.getId());
    public void updateRating(int userId) {
        String sql = "UPDATE user u SET u.rating = ("
                + " SELECT COALESCE(AVG(r.score), 0.0) FROM review r WHERE r.to_user_id=?"
                + ") WHERE u.id=?";
            ps.setInt(1, userId);
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setDisplayName(rs.getString("display_name"));
        u.setAvatar(rs.getString("avatar"));
        u.setSkills(rs.getString("skills"));
        u.setRating(rs.getDouble("rating"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
}
