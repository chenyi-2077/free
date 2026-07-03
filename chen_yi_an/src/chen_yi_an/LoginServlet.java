package chen_yi_an;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/chen_yi_an/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "邮箱和密码不能为空");
            request.getRequestDispatcher("/chen_yi_an/login.jsp").forward(request, response);
            return;
        }

        User user = userDao.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            request.setAttribute("error", "邮箱或密码错误");
            request.getRequestDispatcher("/chen_yi_an/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/projects");
    }
}
