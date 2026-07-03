package com.freelite.servlet;

import com.freelite.dao.ProjectDao;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyProjectsServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 我开发的项目（作为雇主发布的）
        req.setAttribute("myProjects", projectDao.findByEmployerId(loginUser.getId()));
        // 我竞标的项目（作为自由职业者投过竞标的）
        req.setAttribute("biddedProjects", projectDao.findBiddedProjects(loginUser.getId()));

        req.getRequestDispatcher("/B-project/myProjects.jsp").forward(req, resp);
    }
}
