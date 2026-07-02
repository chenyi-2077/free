<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    List<Bid> bids = (List<Bid>) request.getAttribute("bids");
    int projectId = (int) request.getAttribute("projectId");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>竞标列表 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects">Freelite</a>
            <a href="<%= request.getContextPath() %>/projects" class="text-decoration-none text-muted">← 返回项目列表</a>
        </div>
    </nav>

    <div class="container mt-4">
        <h4 class="fw-bold mb-4">📩 项目 #<%= projectId %> 的竞标</h4>

        <% if (bids == null || bids.isEmpty()) { %>
            <div class="text-center py-5">
                <div style="font-size: 3rem;">📭</div>
                <p class="text-muted mt-3">暂无竞标</p>
            </div>
        <% } else { %>
            <% for (Bid bid : bids) { %>
                <div class="card p-3 mb-2">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <strong><%= bid.getFreelancerName() %></strong>
                            <span class="badge bg-light text-dark ms-2">⭐ <%= bid.getFreelancerRating() %></span>
                            <p class="text-muted small mt-1 mb-0"><%= bid.getProposal() != null ? bid.getProposal() : "无方案描述" %></p>
                        </div>
                        <div class="text-end">
                            <span class="fw-bold" style="color: #28a745;">¥<%= String.format("%.0f", bid.getAmount()) %></span>
                            <small class="text-muted ms-1"><%= bid.getDays() %> 天</small>
                            <div class="mt-1">
                                <% if ("pending".equals(bid.getStatus())) { %>
                                    <span class="badge bg-warning text-dark">等待中</span>
                                <% } else if ("accepted".equals(bid.getStatus())) { %>
                                    <span class="badge bg-success">已中标</span>
                                <% } else { %>
                                    <span class="badge bg-secondary">未选中</span>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            <% } %>
        <% } %>
    </div>
</body>
</html>
