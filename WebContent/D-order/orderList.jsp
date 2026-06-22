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
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); transition: all 0.2s; }
        .card:hover { transform: translateY(-1px); }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: #667eea;">Freelite</a>
            <div class="d-flex">
                <a href="<%= request.getContextPath() %>/projects" class="text-decoration-none text-muted me-3">项目</a>
                <a href="<%= request.getContextPath() %>/profile" class="text-decoration-none text-muted"><%= loginUser.getDisplayName() %></a>
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
            <% for (Order o : orders) { %>
                <a href="<%= request.getContextPath() %>/order/<%= o.getId() %>" class="text-decoration-none">
                    <div class="card p-3 mb-2">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="fw-bold text-dark mb-1"><%= o.getProjectTitle() %></h6>
                                <p class="text-muted small mb-0">
                                    <%= o.getEmployerId() == loginUser.getId() ? ("雇佣了 " + o.getFreelancerName()) : ("为 " + o.getEmployerName() + " 工作") %>
                                    · ¥<%= String.format("%.0f", o.getAmount()) %>
                                </p>
                            </div>
                            <div class="text-end">
                                <% if ("in_progress".equals(o.getStatus())) { %>
                                    <span class="badge bg-warning text-dark">进行中</span>
                                <% } else if ("completed".equals(o.getStatus())) { %>
                                    <span class="badge bg-success">已完成</span>
                                <% } else { %>
                                    <span class="badge bg-secondary">已取消</span>
                                <% } %>
                                <div class="mt-1"><small class="text-muted"><%= o.getCreatedAt() != null ? o.getCreatedAt().substring(0, 10) : "" %></small></div>
                            </div>
                        </div>
                    </div>
                </a>
            <% } %>
        <% } %>
    </div>
</body>
</html>
