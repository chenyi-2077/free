package chen_yi_an;
import chen_yi_an.*;
import chen_yi_an.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;
import chen_kai_bo.Project;
import chen_kai_bo.ProjectDao;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

/**
 * GET /api/myChatProjects — 返回当前用户参与的所有项目（雇主 or 中标 freelancer）
 * 返回 JSON 格式包含项目信息、订单状态、交付物统计和最近消息
 */
public class MyChatProjectsServlet extends HttpServlet {

    private ProjectDao projectDao = new ProjectDao();
    private OrderDao orderDao = new OrderDao();
    private ProjectMessageDao messageDao = new ProjectMessageDao();
    private DeliveryDao deliveryDao = new DeliveryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            out.print("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = loginUser.getId();
        Set<Integer> projectIds = new LinkedHashSet<>();

        // 作为雇主发布的项目
        List<Project> myProjects = projectDao.findByEmployerId(userId);
        for (Project p : myProjects) projectIds.add(p.getId());

        // 作为中标 freelancer 参与的项目
        List<Order> allOrders = orderDao.findByUserId(userId);
        for (Order o : allOrders) {
            if (o.getFreelancerId() == userId) {
                projectIds.add(o.getProjectId());
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int pid : projectIds) {
            Project p = projectDao.findById(pid);
            if (p == null) continue;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", p.getId());
            item.put("title", p.getTitle());
            item.put("employerName", p.getEmployerName());
            item.put("employerId", p.getEmployerId());
            item.put("status", p.getStatus());

            // 订单信息
            List<Order> projectOrders = orderDao.findByProject(pid);
            Map<String, Object> orderInfo = null;
            String userRole = "employer";
            boolean hasDeliveries = false;
            int deliveryCount = 0;

            if (projectOrders != null && !projectOrders.isEmpty()) {
                Order firstOrder = projectOrders.get(0);
                orderInfo = new LinkedHashMap<>();
                orderInfo.put("id", firstOrder.getId());
                orderInfo.put("status", firstOrder.getStatus());
                orderInfo.put("amount", firstOrder.getAmount());
                orderInfo.put("escrowAmount", firstOrder.getEscrowAmount());
                orderInfo.put("freelancerId", firstOrder.getFreelancerId());

                // 判断当前用户在项目中的角色
                if (loginUser.getId() == firstOrder.getEmployerId()) {
                    userRole = "employer";
                } else if (loginUser.getId() == firstOrder.getFreelancerId()) {
                    userRole = "freelancer";
                }

                // 交付物统计
                List<Delivery> deliveries = deliveryDao.findByProjectId(pid);
                deliveryCount = deliveries != null ? deliveries.size() : 0;
                hasDeliveries = deliveryCount > 0;
            } else {
                // 没有订单 -> 当前用户是雇主且项目还没人中标，或者是竞标者未中标
                userRole = "employer";
            }

            item.put("order", orderInfo);
            item.put("userRole", userRole);
            item.put("deliveryCount", deliveryCount);

            // 取最近一条消息作为预览
            List<ProjectMessage> msgs = messageDao.findByProjectId(pid);
            if (msgs != null && !msgs.isEmpty()) {
                ProjectMessage last = msgs.get(msgs.size() - 1);
                Map<String, Object> lastMsg = new LinkedHashMap<>();
                lastMsg.put("content", last.getContent());
                lastMsg.put("senderName", last.getSenderName());
                lastMsg.put("senderId", last.getSenderId());
                if (last.getCreatedAt() != null) {
                    lastMsg.put("time", last.getCreatedAt().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));
                }
                item.put("lastMsg", lastMsg);
                // 如果有未读消息（非当前用户发的最后一条）
                item.put("unread", last.getSenderId() != userId);
            } else {
                item.put("lastMsg", null);
                item.put("unread", false);
            }

            result.add(item);
        }

        // 排序：有最新消息的排在前面
        result.sort((a, b) -> {
            Map<String, Object> ma = (Map<String, Object>) a.get("lastMsg");
            Map<String, Object> mb = (Map<String, Object>) b.get("lastMsg");
            if (ma != null && mb == null) return -1;
            if (ma == null && mb != null) return 1;
            return 0;
        });

        StringBuilder json = new StringBuilder("{\"projects\":[");
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) json.append(",");
            Map<String, Object> item = result.get(i);
            json.append("{");
            json.append("\"id\":").append(item.get("id")).append(",");
            json.append("\"title\":").append(jsonEscape((String) item.get("title"))).append(",");
            json.append("\"employerName\":").append(jsonEscape((String) item.get("employerName"))).append(",");
            json.append("\"employerId\":").append(item.get("employerId")).append(",");
            json.append("\"status\":").append(jsonEscape((String) item.get("status"))).append(",");
            json.append("\"userRole\":").append(jsonEscape((String) item.get("userRole"))).append(",");
            json.append("\"deliveryCount\":").append(item.get("deliveryCount")).append(",");
            json.append("\"unread\":").append(item.get("unread"));

            // Order info
            Map<String, Object> orderInfo = (Map<String, Object>) item.get("order");
            if (orderInfo != null) {
                json.append(",\"order\":{");
                json.append("\"id\":").append(orderInfo.get("id")).append(",");
                json.append("\"status\":").append(jsonEscape((String) orderInfo.get("status"))).append(",");
                json.append("\"amount\":").append(orderInfo.get("amount")).append(",");
                json.append("\"escrowAmount\":").append(orderInfo.get("escrowAmount")).append(",");
                json.append("\"freelancerId\":").append(orderInfo.get("freelancerId"));
                json.append("}");
            } else {
                json.append(",\"order\":null");
            }

            Map<String, Object> lastMsg = (Map<String, Object>) item.get("lastMsg");
            if (lastMsg != null) {
                json.append(",\"lastMsg\":{");
                json.append("\"content\":").append(jsonEscape((String) lastMsg.get("content"))).append(",");
                json.append("\"senderName\":").append(jsonEscape((String) lastMsg.get("senderName"))).append(",");
                json.append("\"senderId\":").append(lastMsg.get("senderId")).append(",");
                json.append("\"time\":").append(jsonEscape((String) lastMsg.get("time")));
                json.append("}");
            } else {
                json.append(",\"lastMsg\":null");
            }
            json.append("}");
        }
        json.append("]}");

        out.print(json.toString());
    }

    private String jsonEscape(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
    }
}
