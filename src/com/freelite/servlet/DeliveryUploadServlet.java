package com.freelite.servlet;

import com.freelite.dao.DeliveryDao;
import com.freelite.model.Delivery;
import com.freelite.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 交付物上传
 * 自由职业者在订单详情页上传交付文件
 */
public class DeliveryUploadServlet extends HttpServlet {

    private DeliveryDao deliveryDao = new DeliveryDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        if (orderIdStr == null || orderIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);

        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setUserId(loginUser.getId());
        delivery.setTitle(title != null ? title : "");
        delivery.setDescription(description != null ? description : "");

        // 处理文件上传（从 multipart data）
        try {
            // 检查是否是 multipart 请求
            String contentType = req.getContentType();
            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                // 使用 commons-fileupload 或直接解析
                // 简单方案：用 Part API（Servlet 3.0+）
                javax.servlet.http.Part filePart = req.getPart("file");
                if (filePart != null && filePart.getSize() > 0) {
                    String originalName = filePart.getSubmittedFileName();
                    if (originalName != null && !originalName.isEmpty()) {
                        // 安全文件名
                        String ext = "";
                        int dotIdx = originalName.lastIndexOf('.');
                        if (dotIdx > 0) ext = originalName.substring(dotIdx);
                        String safeName = UUID.randomUUID().toString() + ext;

                        // 存储路径：WEB-INF/uploads/yyyy/MM/
                        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
                        String uploadDir = getServletContext().getRealPath("/WEB-INF/uploads/" + datePath);
                        new File(uploadDir).mkdirs();

                        String filePath = datePath + "/" + safeName;
                        filePart.write(uploadDir + "/" + safeName);

                        delivery.setFileName(originalName);
                        delivery.setFilePath(filePath);
                        delivery.setFileSize(filePart.getSize());
                        delivery.setFileType(filePart.getContentType());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        deliveryDao.insert(delivery);
        req.getSession().setAttribute("successMsg", "✅ 交付物已上传");
        resp.sendRedirect(req.getContextPath() + "/order/" + orderId);
    }
}
