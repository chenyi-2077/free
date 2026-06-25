<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    List<Bid> bids = (List<Bid>) request.getAttribute("bids");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的竞标 - Freelite</title>
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
                <a href="${pageContext.request.contextPath}/orders" class="nav-link">订单</a>
                <a href="${pageContext.request.contextPath}/wallet" class="nav-link">钱包</a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h4 class="fw-bold mb-4">📩 我的竞标</h4>

        <%
            if (bids == null || bids.isEmpty()) {
        %>
            <div class="text-center py-5">
                <div style="font-size: 3rem;">📭</div>
                <p class="text-muted mt-3">还没有投递过竞标</p>
                <a href="${pageContext.request.contextPath}/projects" class="btn btn-gradient">去看看项目</a>
            </div>
        <%
            } else {
                for (Bid bid : bids) {
        %>
            <div class="card p-3 mb-2">
                <div class="d-flex justify-content-between align-items-center">
                    <div style="flex: 1;">
                        <h6 class="fw-bold mb-1">
                            <a href="${pageContext.request.contextPath}/project/<%= bid.getProjectId() %>" class="text-decoration-none" style="color: #333;">
                                项目 #<%= bid.getProjectId() %>
                            </a>
                        </h6>
                        <span class="fw-bold" style="color: #28a745;">¥<%= String.format("%.0f", bid.getAmount()) %></span>
                        <span class="text-muted ms-2"><%= bid.getDays() %> 天</span>
                        <p class="text-muted small mt-1 mb-0">
                            <%= bid.getProposal() != null && bid.getProposal().length() > 80 ? bid.getProposal().substring(0, 80) + "..." : bid.getProposal() %>
                        </p>
                    </div>
                    <div class="text-end ms-3">
                        <% if ("pending".equals(bid.getStatus())) { %>
                            <span class="badge bg-warning text-dark">等待雇主</span>
                        <% } else if ("accepted".equals(bid.getStatus())) { %>
                            <span class="badge bg-success">已中标 ✓</span>
                        <% } else { %>
                            <span class="badge bg-secondary">未选中</span>
                        <% } %>
                        <div class="mt-1" style="font-size: 0.8rem; color: #999;">
                            <%= bid.getCreatedAt() != null ? bid.getCreatedAt().toLocalDate().toString() : "" %>
                        </div>
                    </div>
                </div>
            </div>
        <%
                }
            }
        %>
    </div>
</body>
</html>
