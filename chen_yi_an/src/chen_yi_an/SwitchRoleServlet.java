package chen_yi_an;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SwitchRoleServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<html><body style='font-family:sans-serif;text-align:center;padding:80px 20px;'>"
                + "<h2 style='color:#999;'>🔒 请先登录</h2>"
                + "<p style='color:#666;font-size:16px;'>需要登录后才能切换角色。</p>"
                + "<a href='" + req.getContextPath() + "/login' style='color:#667eea;'>← 去登录</a>"
                + "</body></html>");
            return;
        }

        String newRole = req.getParameter("role");
        if (newRole == null || (!"employer".equals(newRole) && !"freelancer".equals(newRole))) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // 更新数据库
        userDao.updateRole(loginUser.getId(), newRole);

        // 更新 session 中的用户信息
        loginUser.setRole(newRole);
        req.getSession().setAttribute("user", loginUser);

        // 跳回个人主页
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
