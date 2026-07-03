package chen_yi_an;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/chen_yi_an/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String displayName = request.getParameter("displayName");
        String skills = request.getParameter("skills");

        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "邮箱和密码不能为空");
            request.getRequestDispatcher("/chen_yi_an/register.jsp").forward(request, response);
            return;
        }

        if (userDao.findByEmail(email) != null) {
            request.setAttribute("error", "该邮箱已被注册");
            request.getRequestDispatcher("/chen_yi_an/register.jsp").forward(request, response);
            return;
        }

        User user = new User(email, password, role, displayName, skills);
        int id = userDao.insert(user);
        if (id > 0) {
            user.setId(id);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/profile");
        } else {
            request.setAttribute("error", "注册失败，请重试");
            request.getRequestDispatcher("/chen_yi_an/register.jsp").forward(request, response);
        }
    }
}
