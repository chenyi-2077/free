<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的订单 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/projects" class="nav-link">项目</a>
                <a href="${pageContext.request.contextPath}/my/projects" class="nav-link">我的项目</a>
                <a href="${pageContext.request.contextPath}/wallet" class="nav-link">钱包</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">看板</a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-link">个人主页</a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h4 class="fw-bold mb-4">📦 我的订单</h4>

        <%
            if (orders == null || orders.isEmpty()) {
        %>
            <div class="text-center py-5">
                <div style="font-size: 3rem;">📭</div>
                <p class="text-muted mt-3">暂无订单</p>
                <a href="${pageContext.request.contextPath}/projects" class="btn btn-gradient">去看看项目</a>
            </div>
        <%
            } else {
                for (Order o : orders) {
        %>
            <a href="${pageContext.request.contextPath}/order/<%= o.getId() %>" class="text-decoration-none">
                <div class="card p-3 mb-2">
                    <div class="d-flex justify-content-between align-items-center">
                        <div style="flex: 1;">
                            <h6 class="fw-bold text-dark mb-1"><%= o.getProjectTitle() %></h6>
                            <p class="text-muted small mb-0">
                                <%= o.getEmployerId() == loginUser.getId()
                                    ? ("👥 雇佣了 " + o.getFreelancerName())
                                    : ("💼 为 " + o.getEmployerName() + " 工作") %>
                                · ¥<%= String.format("%.0f", o.getAmount()) %>
                            </p>
                        </div>
                        <div class="text-end ms-3">
                            <% if ("in_progress".equals(o.getStatus())) { %>
                                <span class="badge bg-warning text-dark">进行中</span>
                            <% } else if ("completed".equals(o.getStatus())) { %>
                                <span class="badge bg-success">已完成 ✓</span>
                            <% } else { %>
                                <span class="badge bg-secondary">已取消</span>
                            <% } %>
                            <div class="mt-1" style="font-size: 0.8rem; color: #999;">
                                <%= o.getCreatedAt() != null ? o.getCreatedAt().toLocalDate().toString() : "" %>
                            </div>
                        </div>
                    </div>
                </div>
            </a>
        <%
                }
            }
        %>
    </div>
</body>
</html>
