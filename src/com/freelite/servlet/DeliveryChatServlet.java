package com.freelite.servlet;

import com.freelite.dao.*;
import com.freelite.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes; // not needed, use Files
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 统一的交付与沟通页面
 * GET  /deliveryChat?projectId=X — 展示页面
 * POST /deliveryChat?action=message — 发送消息
 * POST /deliveryChat?action=upload — 上传交付物
 * 
 * 文件存储路径可通过 web.xml init-param uploadDir 配置，
 * 默认 /home/admin/.openclaw/workspace/freelite-uploads/ （Docker volume 挂载路径）
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 50,       // 50MB
    maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class DeliveryChatServlet extends HttpServlet {

    private static final String DEFAULT_UPLOAD_DIR = "/home/admin/.openclaw/workspace/freelite-uploads";
    private static final long THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000;
    private static final double MAX_DISK_USAGE = 0.80; // 80% 磁盘使用上限
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

    private ProjectDao projectDao = new ProjectDao();
    private ProjectMessageDao messageDao = new ProjectMessageDao();
    private DeliveryDao deliveryDao = new DeliveryDao();
    private OrderDao orderDao = new OrderDao();

    private String getUploadBaseDir() {
        // 优先用外部路径
        File extDir = new File(DEFAULT_UPLOAD_DIR);
        if (extDir.exists() || extDir.mkdirs()) {
            return DEFAULT_UPLOAD_DIR;
        }
        // 回退到 webapp 内部（Docker 无外部 volume 时）
        return getServletContext().getRealPath("/WEB-INF/uploads");
    }

    @Override
    public void init() throws ServletException {
        // 启动时清理超过30天的旧文件
        new Thread(() -> {
            try {
                Thread.sleep(30000); // 启动后30秒执行
                cleanupOldFiles();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void cleanupOldFiles() {
        String baseDir = getUploadBaseDir();
        File base = new File(baseDir);
        if (!base.exists()) return;
        long cutoff = System.currentTimeMillis() - THIRTY_DAYS_MS;
        try {
            Files.walk(base.toPath())
                .filter(Files::isRegularFile)
                .filter(p -> {
                    try {
                        return Files.getLastModifiedTime(p).toMillis() < cutoff;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(p -> {
                    try {
                        Files.delete(p);
                        System.out.println("[Cleanup] Deleted old file: " + p);
                    } catch (IOException e) {
                        System.err.println("[Cleanup] Failed to delete: " + p);
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);

        // 获取项目信息
        Project project = projectDao.findById(projectId);
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        // 权限检查：只有雇主或中标自由职业者能看
        if (!hasAccess(loginUser, project)) {
            req.getSession().setAttribute("errorMsg", "❌ 您无权访问该项目的沟通与交付页面，只有项目雇主和中标自由职业者可查看。");
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        List<ProjectMessage> messages = messageDao.findByProjectId(projectId);
        List<Delivery> deliveries = deliveryDao.findByProjectId(projectId);

        req.setAttribute("projectId", projectId);
        req.setAttribute("project", project);
        req.setAttribute("projectTitle", project.getTitle());
        req.setAttribute("messages", messages);
        req.setAttribute("deliveries", deliveries);
        req.getRequestDispatcher("/deliveryChat.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String projectIdStr = req.getParameter("projectId");
        String action = req.getParameter("action");
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }
        int projectId = Integer.parseInt(projectIdStr);

        // 权限检查：只有雇主或中标自由职业者能操作
        Project project = projectDao.findById(projectId);
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }
        if (!hasAccess(loginUser, project)) {
            req.getSession().setAttribute("errorMsg", "❌ 您无权访问该项目的沟通与交付页面。");
            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        if ("message".equals(action)) {
            // 发送消息
            String content = req.getParameter("content");
            if (content != null && !content.trim().isEmpty()) {
                ProjectMessage msg = new ProjectMessage();
                msg.setProjectId(projectId);
                msg.setSenderId(loginUser.getId());
                msg.setContent(content.trim());
                messageDao.insert(msg);
            }
        } else if ("upload".equals(action)) {
            // 上传交付物
            String title = req.getParameter("title");
            String description = req.getParameter("description");

            Delivery delivery = new Delivery();
            delivery.setProjectId(projectId);
            delivery.setUserId(loginUser.getId());
            delivery.setTitle(title != null ? title : "");
            delivery.setDescription(description != null ? description : "");

            // 磁盘空间检查
            File baseDir = new File(getUploadBaseDir());
            long total = baseDir.getTotalSpace();
            long free = baseDir.getFreeSpace();
            double usage = 1.0 - (double) free / total;
            if (usage > MAX_DISK_USAGE) {
                req.getSession().setAttribute("errorMsg", "❌ 上传失败：磁盘空间不足（已用 " + String.format("%.0f", usage * 100) + "%）");
                resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
                return;
            }

            boolean uploaded = false;
            try {
                Part filePart = req.getPart("file");
                if (filePart != null && filePart.getSize() > 0) {
                    String originalName = filePart.getSubmittedFileName();
                    if (originalName != null && !originalName.isEmpty()) {
                        // 文件类型检查
                        String ext = "";
                        int dotIdx = originalName.lastIndexOf('.');
                        if (dotIdx > 0) ext = originalName.substring(dotIdx).toLowerCase();
                        boolean allowed = false;
                        for (String ae : ALLOWED_EXTENSIONS) {
                            if (ae.equals(ext)) { allowed = true; break; }
                        }
                        if (!allowed) {
                            req.getSession().setAttribute("errorMsg", "❌ 不支持的文件类型：" + ext + "。允许的类型：pdf/zip/rar/doc/jpg/png/txt/md/java/py 等常见格式");
                            resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
                            return;
                        }

                        String safeName = UUID.randomUUID().toString() + ext;

                        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
                        String uploadDir = getUploadBaseDir() + "/" + datePath;
                        new File(uploadDir).mkdirs();

                        String filePath = datePath + "/" + safeName;
                        filePart.write(uploadDir + "/" + safeName);

                        delivery.setFileName(originalName);
                        delivery.setFilePath(filePath);
                        delivery.setFileSize(filePart.getSize());
                        delivery.setFileType(filePart.getContentType());
                        uploaded = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.getSession().setAttribute("errorMsg", "❌ 文件上传失败：" + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
                return;
            }

            if (!uploaded) {
                req.getSession().setAttribute("errorMsg", "❌ 上传失败：未选择文件或文件为空");
                resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
                return;
            }

            deliveryDao.insert(delivery);
            req.getSession().setAttribute("successMsg", "✅ 交付物已上传");
        }

        resp.sendRedirect(req.getContextPath() + "/deliveryChat?projectId=" + projectId);
    }

    /**
     * 检查当前用户是否有权访问该项目的沟通/交付页面
     * 项目雇主和中标自由职业者有权限
     */
    private boolean hasAccess(User user, Project project) {
        if (user == null || project == null) return false;
        // 项目雇主
        if (user.getId() == project.getEmployerId()) return true;
        // 中标自由职业者——查 order 表
        List<com.freelite.model.Order> orders = orderDao.findByProject(project.getId());
        if (orders != null) {
            for (com.freelite.model.Order o : orders) {
                if (o.getFreelancerId() == user.getId()) return true;
            }
        }
        return false;
    }
}
