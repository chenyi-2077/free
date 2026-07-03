package com.freelite.servlet;

import com.freelite.dao.UserDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile/edit")
public class EditProfileServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("profileUser", user);
        req.getRequestDispatcher("/A-user/editProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User sessionUser = (User) req.getSession().getAttribute("user");
        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String displayName = req.getParameter("displayName");
        String skills = req.getParameter("skills");

        User user = userDao.findById(sessionUser.getId());
        if (user != null) {
            if (displayName != null && !displayName.trim().isEmpty()) {
                user.setDisplayName(displayName.trim());
            }
            user.setSkills(skills);
            userDao.update(user);
            req.getSession().setAttribute("user", user);
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
