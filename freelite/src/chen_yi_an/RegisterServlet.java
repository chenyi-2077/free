package chen_yi_an;

import chen_yi_an.UserDao;
import chen_yi_an.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String displayName = req.getParameter("displayName");
        String role = req.getParameter("role");

        if (email == null || password == null || displayName == null || role == null ||
            email.trim().isEmpty() || password.trim().isEmpty() || displayName.trim().isEmpty()) {
            req.setAttribute("error", "请填写所有必填字段");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
            return;
        }

        User existing = userDao.findByEmail(email.trim());
        if (existing != null) {
            req.setAttribute("error", "该邮箱已被注册");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setEmail(email.trim());
        user.setPassword(password.trim());
        user.setDisplayName(displayName.trim());
        user.setRole(role);
        user.setRating(0);

        int id = userDao.insert(user);
        if (id > 0) {
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
        } else {
            req.setAttribute("error", "注册失败，请重试");
            req.getRequestDispatcher("/A-user/register.jsp").forward(req, resp);
        }
    }
}
