package chen_yi_an;

import com.freelite.util.DBUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeliveryChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                request.setAttribute("user", user);

                // Get projects the user is participating in
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    conn = DBUtil.getConnection();
                    String sql = "SELECT p.id, p.title, p.description, p.progress, p.status " +
                                 "FROM projects p " +
                                 "JOIN project_members pm ON p.id = pm.project_id " +
                                 "WHERE pm.user_id = ? " +
                                 "ORDER BY p.updated_at DESC";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, user.getId());
                    rs = ps.executeQuery();
                    List<Map<String, Object>> projects = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> project = new java.util.HashMap<>();
                        project.put("id", rs.getInt("id"));
                        project.put("title", rs.getString("title"));
                        project.put("description", rs.getString("description"));
                        project.put("progress", rs.getInt("progress"));
                        project.put("status", rs.getString("status"));
                        projects.add(project);
                    }
                    request.setAttribute("projects", projects);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("projects", new ArrayList<>());
                } finally {
                    DBUtil.close(rs, ps, conn);
                }
            }
        }
        request.getRequestDispatcher("/chen_yi_an/deliveryChat.jsp").forward(request, response);
    }
}
