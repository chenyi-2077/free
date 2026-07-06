package chen_yi_an;
import chen_yi_an.*;
import chen_yi_an.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;
import chen_kai_bo.Project;
import chen_kai_bo.ProjectDao;
import chen_zi_hao.OrderDao;
import chen_zi_hao.Order;

/**
 * 聊天弹窗的 API 端点
 * GET  /api/chatMessages?projectId=X — 返回该项目的消息列表（JSON）
 * POST /api/chatMessages — 发送消息（form-data: projectId, content）
 */
public class ChatMessageApiServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private ProjectMessageDao messageDao = new ProjectMessageDao();
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            out.print("{\"error\":\"not logged in\"}");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            out.print("{\"error\":\"missing projectId\"}");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        Project project = projectDao.findById(projectId);
        if (project == null) {
            out.print("{\"error\":\"project not found\"}");
            return;
        }

        // 权限检查：只有项目参与者能看
        boolean hasAccess = (loginUser.getId() == project.getEmployerId());
        if (!hasAccess) {
            List<Order> orders = orderDao.findByProject(projectId);
            for (Order o : orders) {
                if (o.getFreelancerId() == loginUser.getId()) {
                    hasAccess = true;
                    break;
                }
            }
        }
        if (!hasAccess) {
            out.print("{\"error\":\"access denied\"}");
            return;
        }

        List<ProjectMessage> msgs = messageDao.findByProjectId(projectId);
        StringBuilder json = new StringBuilder("{\"messages\":[");
        for (int i = 0; i < msgs.size(); i++) {
            if (i > 0) json.append(",");
            ProjectMessage m = msgs.get(i);
            json.append("{");
            json.append("\"id\":").append(m.getId()).append(",");
            json.append("\"senderName\":").append(jsonEscape(m.getSenderName())).append(",");
            json.append("\"senderId\":").append(m.getSenderId()).append(",");
            json.append("\"content\":").append(jsonEscape(m.getContent())).append(",");
            json.append("\"isSelf\":").append(m.getSenderId() == loginUser.getId());
            if (m.getCreatedAt() != null) {
                json.append(",\"time\":").append(jsonEscape(m.getCreatedAt().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))));
            } else {
                json.append(",\"time\":null");
            }
            json.append("}");
        }
        json.append("]}");
        out.print(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            out.print("{\"error\":\"not logged in\"}");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        String content = req.getParameter("content");

        if (projectIdStr == null || projectIdStr.isEmpty() || content == null || content.trim().isEmpty()) {
            out.print("{\"error\":\"missing parameters\"}");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        Project project = projectDao.findById(projectId);
        if (project == null) {
            out.print("{\"error\":\"project not found\"}");
            return;
        }

        // 权限检查
        boolean hasAccess = (loginUser.getId() == project.getEmployerId());
        if (!hasAccess) {
            List<Order> orders = orderDao.findByProject(projectId);
            for (Order o : orders) {
                if (o.getFreelancerId() == loginUser.getId()) {
                    hasAccess = true;
                    break;
                }
            }
        }
        if (!hasAccess) {
            out.print("{\"error\":\"access denied\"}");
            return;
        }

        ProjectMessage msg = new ProjectMessage();
        msg.setProjectId(projectId);
        msg.setSenderId(loginUser.getId());
        msg.setContent(content.trim());
        messageDao.insert(msg);

        out.print("{\"success\":true}");
    }

    private String jsonEscape(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
    }
}
