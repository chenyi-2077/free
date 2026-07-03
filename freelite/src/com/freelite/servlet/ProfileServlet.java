package com.freelite.servlet;

import com.freelite.dao.ReviewDao;
import com.freelite.dao.UserDao;
import com.freelite.model.Review;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private UserDao userDao = new UserDao();
    private ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        User sessionUser = (User) req.getSession().getAttribute("user");

        User profileUser = null;
        if (idParam != null && !idParam.trim().isEmpty()) {
            profileUser = userDao.findById(Integer.parseInt(idParam));
        } else if (sessionUser != null) {
            profileUser = userDao.findById(sessionUser.getId());
        }

        if (profileUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Review> reviews = reviewDao.findByToUserId(profileUser.getId());
        double avgRating = reviews.stream().mapToInt(Review::getScore).average().orElse(0);

        req.setAttribute("profileUser", profileUser);
        req.setAttribute("reviews", reviews);
        req.setAttribute("avgRating", avgRating);
        req.getRequestDispatcher("/A-user/profile.jsp").forward(req, resp);
    }
}
