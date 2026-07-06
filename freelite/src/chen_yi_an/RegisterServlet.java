package chen_yi_an;
import chen_kai_bo.Project;
import chen_kai_bo.Bid;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Category;
import chen_xi_rui.BidDao;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

import chen_yi_an.UserDao;
import chen_yi_an.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class RegisterServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");
        // 检查邮箱是否已注册
        if (userDao.findByEmail(email) != null) {
            req.setAttribute("error", "该邮箱已被注册");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
            return;
        }
        User user = new User(email, password, role, displayName, skills);
        int id = userDao.insert(user);
        if (id > 0) {
            // 注册成功，自动登录
            user.setId(id);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/projects");
        } else {
            req.setAttribute("error", "注册失败，请重试");
}
