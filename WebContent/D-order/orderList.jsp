<%@ page import="java.util.List, com.freelite.model.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的订单 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <div class="d-flex gap-3">
                <a href="${pageContext.request.contextPath}/projects" class="nav-link">项目市场</a>
                <a href="${pageContext.request.contextPath}/orders" class="nav-link">订单</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h4 class="fw-bold mb-4">📦 我的订单</h4>

        <% if (orders == null || orders.isEmpty()) { %>
            <div class="text-center py-5">
                <div style="font-size: 3rem;">📭</div>
                <p class="text-muted mt-3">暂无订单</p>
            </div>
        <% } else { %>
            <table class="table table-hover align-middle">
                <thead style="background: var(--okx-bg-secondary);">
                    <tr>
                        <th>订单ID</th>
                        <th>金额</th>
                        <th>状态</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Order o : orders) { %>
                        <tr>
                            <td>#<%= o.getId() %></td>
                            <td>¥<%= String.format("%.2f", o.getAmount()) %></td>
                            <td>
                                <% String s = o.getStatus(); %>
                                <% if ("in_progress".equals(s)) { %>
                                    <span class="badge bg-primary">进行中</span>
                                <% } else if ("completed".equals(s)) { %>
                                    <span class="badge bg-success">已完成</span>
                                <% } else if ("cancelled".equals(s)) { %>
                                    <span class="badge bg-secondary">已取消</span>
                                <% } else { %>
                                    <span class="badge bg-warning text-dark"><%= s %></span>
                                <% } %>
                            </td>
                            <td><%= o.getCreatedAt() != null ? o.getCreatedAt().toLocalDate().toString() : "-" %></td>
                        <td>
                            <% if ("completed".equals(o.getStatus()) || "in_progress".equals(o.getStatus())) { %>
                                <a href="${pageContext.request.contextPath}/review?orderId=<%= o.getId() %>" class="btn btn-sm btn-outline-primary">去评价</a>
                            <% } else { %>
                                <span class="text-muted">-</span>
                            <% } %>
                        </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>
