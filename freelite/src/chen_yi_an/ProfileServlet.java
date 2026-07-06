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
public class ProfileServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // 从路径提取用户 ID，如 /profile/2 或 /profile
        String pathInfo = req.getPathInfo();
        int profileUserId;
        boolean isOwnProfile;
        if (pathInfo == null || pathInfo.equals("/")) {
            // 查看自己的主页
            profileUserId = loginUser.getId();
            isOwnProfile = true;
        } else {
            try {
                profileUserId = Integer.parseInt(pathInfo.replace("/", ""));
            } catch (NumberFormatException e) {
                profileUserId = loginUser.getId();
            }
            isOwnProfile = (profileUserId == loginUser.getId());
        User profileUser = userDao.findById(profileUserId);
        if (profileUser == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
        req.setAttribute("profileUser", profileUser);
        req.setAttribute("isOwnProfile", isOwnProfile);
        req.getRequestDispatcher("/A-user/profile.jsp").forward(req, resp);
    }
}
