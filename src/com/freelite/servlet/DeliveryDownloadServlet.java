package com.freelite.servlet;

import com.freelite.dao.DeliveryDao;
import com.freelite.model.Delivery;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.annotation.WebServlet;

/**
 * 下载交付物
 */
public class DeliveryDownloadServlet extends HttpServlet {

    private DeliveryDao deliveryDao = new DeliveryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendError(404);
            return;
        }

        int deliveryId = Integer.parseInt(idStr);
        Delivery delivery = deliveryDao.findById(deliveryId);

        if (delivery == null || delivery.getFilePath() == null || delivery.getFilePath().isEmpty()) {
            resp.sendError(404);
            return;
        }

        String baseDir = "/home/admin/.openclaw/workspace/freelite-uploads";
        File file = new File(baseDir + "/" + delivery.getFilePath());
        // 回退 webapp 内部路径
        if (!file.exists()) {
            file = new File(getServletContext().getRealPath("/WEB-INF/uploads/" + delivery.getFilePath()));
        }
        if (!file.exists()) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<html><body style='font-family:sans-serif;text-align:center;padding:80px 20px;'>"
                + "<h2 style='color:#999;'>📁 文件不可用</h2>"
                + "<p style='color:#666;font-size:16px;'>该交付物文件已过期或已被清理。</p>"
                + "<p style='color:#999;font-size:14px;'>Freelite 自动清理超过30天的交付物文件，但记录保留。</p>"
                + "<a href='javascript:history.back()' style='color:#667eea;'>← 返回</a>"
                + "</body></html>");
            return;
        }

        resp.setContentType(delivery.getFileType() != null ? delivery.getFileType() : "application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(delivery.getFileName(), "UTF-8"));
        resp.setContentLengthLong(file.length());

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        }
    }
}
