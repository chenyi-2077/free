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

        String realPath = getServletContext().getRealPath("/WEB-INF/uploads/" + delivery.getFilePath());
        File file = new File(realPath);
        if (!file.exists()) {
            resp.sendError(404);
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
