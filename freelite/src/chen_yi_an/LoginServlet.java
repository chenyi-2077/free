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
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 已登录则跳转项目列表
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }
        req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = userDao.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("error", "邮箱或密码错误");
            req.getRequestDispatcher("/A-user/login.jsp").forward(req, resp);
        req.getSession().setAttribute("user", user);
        // 如果有 redirect 参数则跳转
        String redirect = req.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + redirect);
        } else {
}
