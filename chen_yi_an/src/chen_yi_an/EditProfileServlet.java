package chen_yi_an;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/chen_yi_an/editProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 独立版本：如果session有user就更新，无则提示
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            request.setAttribute("error", "请先登录后再编辑资料");
            request.getRequestDispatcher("/chen_yi_an/editProfile.jsp").forward(request, response);
            return;
        }

        String displayName = request.getParameter("displayName");
        String skills = request.getParameter("skills");

        if (displayName != null && !displayName.trim().isEmpty()) {
            user.setDisplayName(displayName);
        }
        user.setSkills(skills);

        boolean success = userDao.update(user);
        if (success) {
            session.setAttribute("user", user);
            request.setAttribute("success", "资料更新成功");
        } else {
            request.setAttribute("error", "资料更新失败");
        }

        request.getRequestDispatcher("/chen_yi_an/editProfile.jsp").forward(request, response);
    }
}
