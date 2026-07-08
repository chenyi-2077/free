package chen_yi_an;
import chen_yi_an.*;
import chen_yi_an.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import chen_kai_bo.Project;
import chen_kai_bo.ProjectDao;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

/**
 * 聊天弹窗的交付和订单操作 API
 * GET  /api/chatDelivery?projectId=X — 返回交付物列表
 * POST /api/chatDelivery?action=upload&projectId=X — 上传交付物（multipart）
 * POST /api/chatDelivery?action=complete&projectId=X — 自由职业者标记完成
 * POST /api/chatDelivery?action=confirm&projectId=X — 雇主确认完成
 */
public class ChatDeliveryApiServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private DeliveryDao deliveryDao = new DeliveryDao();
    private OrderDao orderDao = new OrderDao();
    private ProjectMessageDao messageDao = new ProjectMessageDao();

    private static final String[] ALLOWED_EXTENSIONS = {
        ".pdf", ".zip", ".rar", ".7z", ".tar", ".gz",
        ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp",
        ".txt", ".md", ".csv",
        ".js", ".css", ".html", ".xml", ".json",
        ".java", ".py", ".php", ".sql", ".sh",
        ".mp3", ".mp4", ".mov",
        ".psd", ".ai", ".fig", ".sketch",
        ".apk", ".ipa", ".exe"
    };

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

        List<Delivery> deliveries = deliveryDao.findByProjectId(projectId);
        StringBuilder json = new StringBuilder("{\"deliveries\":[");
        for (int i = 0; i < deliveries.size(); i++) {
            if (i > 0) json.append(",");
            Delivery d = deliveries.get(i);
            json.append("{");
            json.append("\"id\":").append(d.getId()).append(",");
            json.append("\"title\":").append(jsonEscape(d.getTitle())).append(",");
            json.append("\"fileName\":").append(jsonEscape(d.getFileName())).append(",");
            json.append("\"userName\":").append(jsonEscape(d.getUserName())).append(",");
            json.append("\"description\":").append(jsonEscape(d.getDescription()));
            if (d.getCreatedAt() != null) {
                json.append(",\"time\":").append(jsonEscape(d.getCreatedAt().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))));
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

        String action = req.getParameter("action");
        String projectIdStr = req.getParameter("projectId");

        if (projectIdStr == null || projectIdStr.isEmpty() || action == null) {
            out.print("{\"error\":\"missing parameters\"}");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);

        switch (action) {
            case "upload":
                handleUpload(req, resp, loginUser, projectId, out);
                break;
            case "complete":
                handleComplete(loginUser, projectId, out);
                break;
            case "confirm":
                handleConfirm(loginUser, projectId, out);
                break;
            default:
                out.print("{\"error\":\"unknown action\"}");
        }
    }

    private void handleUpload(HttpServletRequest req, HttpServletResponse resp,
                               User loginUser, int projectId, PrintWriter out) throws IOException {
        try {
            String title = req.getParameter("title");
            String description = req.getParameter("description");

            // 文件上传
            javax.servlet.http.Part filePart = req.getPart("file");
            if (filePart == null || filePart.getSize() == 0) {
                out.print("{\"error\":\"no file uploaded\"}");
                return;
            }

            String originalName = filePart.getSubmittedFileName();
            if (originalName == null || originalName.isEmpty()) {
                out.print("{\"error\":\"invalid file name\"}");
                return;
            }

            // 扩展名校验
            String ext = "";
            int dotIdx = originalName.lastIndexOf('.');
            if (dotIdx > 0) ext = originalName.substring(dotIdx).toLowerCase();
            boolean allowed = false;
            for (String ae : ALLOWED_EXTENSIONS) {
                if (ae.equals(ext)) { allowed = true; break; }
            }
            if (!allowed) {
                out.print("{\"error\":\"unsupported file type: " + jsonEscape(ext) + "\"}");
                return;
            }

            String safeName = UUID.randomUUID().toString() + ext;
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            // 从 web.xml context-param 读取上传目录
            String uploadBase = getServletContext().getInitParameter("uploadDir");
            if (uploadBase == null || uploadBase.isEmpty()) {
                // 没有配置则尝试 Docker volume 路径，不行回退 webapp 内部
                String externalDir = "/home/admin/.openclaw/workspace/freelite-uploads";
                java.io.File extDir = new java.io.File(externalDir);
                if (extDir.exists() || extDir.mkdirs()) {
                    uploadBase = externalDir;
                } else {
                    uploadBase = getServletContext().getRealPath("/WEB-INF/uploads");
                }
            }
            java.io.File uploadDir = new java.io.File(uploadBase, datePath);
            uploadDir.mkdirs();

            java.io.File targetFile = new java.io.File(uploadDir, safeName);
            filePart.write(targetFile.getAbsolutePath());

            String filePath = datePath + "/" + safeName;

            Delivery delivery = new Delivery();
            delivery.setProjectId(projectId);
            delivery.setUserId(loginUser.getId());
            delivery.setTitle(title != null ? title : "");
            delivery.setDescription(description != null ? description : "");
            delivery.setFileName(originalName);
            delivery.setFilePath(filePath);
            delivery.setFileSize(filePart.getSize());
            delivery.setFileType(filePart.getContentType());

            // 查找 order_id
            List<Order> orders = orderDao.findByProject(projectId);
            if (orders != null && !orders.isEmpty()) {
                delivery.setOrderId(orders.get(0).getId());
            }

            deliveryDao.insert(delivery);

            // 发送系统消息
            ProjectMessage msg = new ProjectMessage();
            msg.setProjectId(projectId);
            msg.setSenderId(loginUser.getId());
            msg.setContent("📎 上传了交付物: " + originalName);
            messageDao.insert(msg);

            out.print("{\"success\":true,\"fileName\":" + jsonEscape(originalName) + "}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"upload failed: " + jsonEscape(e.getMessage()) + "\"}");
        }
    }

    private void handleComplete(User loginUser, int projectId, PrintWriter out) {
        Project project = projectDao.findById(projectId);
        if (project == null) {
            out.print("{\"error\":\"project not found\"}");
            return;
        }

        List<Order> orders = orderDao.findByProject(projectId);
        if (orders == null || orders.isEmpty()) {
            out.print("{\"error\":\"no order found\"}");
            return;
        }

        Order order = orders.get(0);
        if (loginUser.getId() != order.getFreelancerId()) {
            out.print("{\"error\":\"only the freelancer can mark complete\"}");
            return;
        }

        if (!"in_progress".equals(order.getStatus())) {
            out.print("{\"error\":\"order is not in progress\"}");
            return;
        }

        orderDao.updateStatus(order.getId(), "awaiting_confirm");

        // 发送系统消息
        ProjectMessage msg = new ProjectMessage();
        msg.setProjectId(projectId);
        msg.setSenderId(loginUser.getId());
        msg.setContent("✅ 自由职业者标记项目已完成，等待雇主确认...");
        messageDao.insert(msg);

        out.print("{\"success\":true,\"newStatus\":\"awaiting_confirm\"}");
    }

    private void handleConfirm(User loginUser, int projectId, PrintWriter out) {
        Project project = projectDao.findById(projectId);
        if (project == null) {
            out.print("{\"error\":\"project not found\"}");
            return;
        }

        List<Order> orders = orderDao.findByProject(projectId);
        if (orders == null || orders.isEmpty()) {
            out.print("{\"error\":\"no order found\"}");
            return;
        }

        Order order = orders.get(0);
        if (loginUser.getId() != order.getEmployerId()) {
            out.print("{\"error\":\"only the employer can confirm\"}");
            return;
        }

        if (!"awaiting_confirm".equals(order.getStatus())) {
            out.print("{\"error\":\"order is not awaiting confirmation\"}");
            return;
        }

        // 释放资金
        com.freelite.service.EscrowService escrowService = new com.freelite.service.EscrowService();
        boolean released = escrowService.releaseToFreelancer(
                projectId, order.getEmployerId(), order.getFreelancerId(), order.getAmount()
        );

        if (!released) {
            out.print("{\"error\":\"escrow release failed\"}");
            return;
        }

        orderDao.updateStatus(order.getId(), "completed");

        // 更新项目状态
        projectDao.updateStatus(projectId, "completed");

        // 发送系统消息
        ProjectMessage msg = new ProjectMessage();
        msg.setProjectId(projectId);
        msg.setSenderId(loginUser.getId());
        msg.setContent("✅ 雇主确认完成，¥" + String.format("%.2f", order.getAmount()) + " 已释放！");
        messageDao.insert(msg);

        out.print("{\"success\":true,\"newStatus\":\"completed\"}");
    }

    private String jsonEscape(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
    }
}
