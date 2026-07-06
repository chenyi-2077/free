package chen_kai_bo;
import chen_xi_rui.BidDao;
import chen_xi_rui.Bid;
import chen_yi_an.EscrowService;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

import chen_kai_bo.CategoryDao;
import chen_kai_bo.ProjectDao;
import chen_kai_bo.Project;
import chen_kai_bo.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
public class EditProjectServlet extends HttpServlet {
    private ProjectDao projectDao = new ProjectDao();
    private CategoryDao categoryDao = new CategoryDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int projectId = Integer.parseInt(req.getParameter("id"));
        Project project = projectDao.findById(projectId);
        if (project == null || project.getEmployerId() != loginUser.getId()) {
            resp.sendRedirect(req.getContextPath() + "/my/projects");
        req.setAttribute("project", project);
        req.setAttribute("categories", categoryDao.findAll());
        req.getRequestDispatcher("/B-project/editProject.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        req.setCharacterEncoding("UTF-8");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String budgetStr = req.getParameter("budget");
        String deadlineStr = req.getParameter("deadline");
        String categoryIdStr = req.getParameter("categoryId");
        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("error", "项目标题不能为空");
            req.setAttribute("project", project);
            req.setAttribute("categories", categoryDao.findAll());
            req.getRequestDispatcher("/B-project/editProject.jsp").forward(req, resp);
        project.setTitle(title.trim());
        project.setDescription(description != null ? description.trim() : "");
        if (budgetStr != null && !budgetStr.isEmpty()) {
            project.setBudget(Double.parseDouble(budgetStr));
        if (deadlineStr != null && !deadlineStr.isEmpty()) {
            project.setDeadline(LocalDate.parse(deadlineStr));
        } else {
            project.setDeadline(null);
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            project.setCategoryId(Integer.parseInt(categoryIdStr));
        projectDao.update(project);
        resp.sendRedirect(req.getContextPath() + "/my/projects");
}
