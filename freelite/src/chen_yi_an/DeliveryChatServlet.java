package chen_yi_an;

import chen_yi_an.DeliveryDao;
import chen_yi_an.ProjectMessageDao;
import chen_yi_an.Delivery;
import chen_yi_an.ProjectMessage;
import chen_yi_an.User;
import chen_kai_bo.Project;
import chen_kai_bo.ProjectDao;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/deliveryChat")
public class DeliveryChatServlet extends HttpServlet {

    private DeliveryDao deliveryDao = new DeliveryDao();
    private ProjectMessageDao messageDao = new ProjectMessageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        List<Delivery> deliveries = deliveryDao.findByProjectId(projectId);
        List<ProjectMessage> messages = messageDao.findByProjectId(projectId);

        ProjectDao projectDao = new ProjectDao();
        OrderDao orderDao = new OrderDao();

        Project project = projectDao.findById(projectId);
        Order order = orderDao.findByProjectId(projectId);
        String myRole = "viewer";
        if (project != null && user.getId() == project.getEmployerId()) {
            myRole = "employer";
        } else if (order != null && user.getId() == order.getFreelancerId()) {
            myRole = "freelancer";
        }

        req.setAttribute("projectId", projectId);
        req.setAttribute("project", project);
        req.setAttribute("projectTitle", project != null ? project.getTitle() : "");
        req.setAttribute("order", order);
        req.setAttribute("myRole", myRole);
        req.setAttribute("deliveries", deliveries);
        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/A-user/deliveryChat.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        int projectId = Integer.parseInt(req.getParameter("projectId"));

        if ("message".equals(action)) {
            String content = req.getParameter("content");
            if (content != null && !content.trim().isEmpty()) {
                ProjectMessage msg = new ProjectMessage();
                msg.setProjectId(projectId);
                msg.setSenderId(user.getId());
                msg.setContent(content.trim());
                messageDao.insert(msg);
            }
        } else if ("delivery".equals(action)) {
            String content = req.getParameter("content");
            Delivery delivery = new Delivery();
            delivery.setProjectId(projectId);
            delivery.setSenderId(user.getId());
            delivery.setContent(content != null ? content.trim() : "");
            delivery.setFileName(req.getParameter("fileName"));
            delivery.setFilePath(req.getParameter("filePath"));
            deliveryDao.insert(delivery);
        }

        resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
    }
}
