<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    Project project = (Project) request.getAttribute("project");
    List<Bid> bids = (List<Bid>) request.getAttribute("bids");
    boolean isOwner = (boolean) request.getAttribute("isOwner");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= project.getTitle() %> - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        .bid-card {
            border-left: 4px solid #667eea;
            border-radius: 0 12px 12px 0;
            transition: all 0.2s;
        }
        .bid-card:hover { background: rgba(102, 126, 234, 0.04); }
    </style>
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/projects" class="nav-link">← 返回</a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-link">个人主页</a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <%-- 主内容 --%>
            <div class="col-md-8">
                <div class="card p-4 mb-4">
                    <div class="d-flex justify-content-between align-items-start">
                        <div style="flex: 1;">
                            <h3 class="fw-bold"><%= project.getTitle() %></h3>
                            <p>
                                <span class="skill-badge"><%= project.getCategoryName() %></span>
                                <span class="text-muted ms-2" style="font-size: 0.9rem;">
                                    <i class="bi bi-person"></i> <%= project.getEmployerName() %>
                                </span>
                                <span class="text-muted ms-2" style="font-size: 0.9rem;">
                                    <i class="bi bi-calendar"></i> <%= project.getCreatedAt() != null ? project.getCreatedAt().substring(0, 10) : "" %>
                                </span>
                                <% if ("open".equals(project.getStatus())) { %>
                                    <span class="badge bg-success ms-2">招募中</span>
                                <% } else if ("in_progress".equals(project.getStatus())) { %>
                                    <span class="badge bg-warning text-dark ms-2">进行中</span>
                                <% } else if ("completed".equals(project.getStatus())) { %>
                                    <span class="badge bg-secondary ms-2">已完成</span>
                                <% } %>
                            </p>
                        </div>
                        <div class="text-end ms-4">
                            <div class="price-tag" style="font-size: 1.5rem;">¥<%= String.format("%.0f", project.getBudget()) %></div>
                            <div style="font-size: 0.85rem; color: #999;">预算</div>
                            <div style="font-size: 0.85rem; color: #999;">截止：<%= project.getDeadline() != null ? project.getDeadline() : "未设置" %></div>
                        </div>
                    </div>
                    <hr>
                    <div style="white-space: pre-wrap; line-height: 1.7;"><%= project.getDescription() != null ? project.getDescription() : "暂无描述" %></div>
                </div>

                <%-- 竞标列表（雇主视角完整版） --%>
                <% if (bids != null && !bids.isEmpty()) { %>
                    <div class="card p-4 mb-4">
                        <h5 class="fw-bold mb-3">📩 收到的竞标（<%= bids.size() %>）</h5>
                        <% for (Bid bid : bids) { %>
                            <div class="card bid-card p-3 mb-2">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div style="flex: 1;">
                                        <strong><%= bid.getFreelancerName() %></strong>
                                        <span class="badge bg-light text-dark ms-1">⭐ <%= bid.getFreelancerRating() %></span>
                                        <p class="text-muted small mt-1 mb-0">
                                            <%= bid.getProposal() != null && bid.getProposal().length() > 120 ? bid.getProposal().substring(0, 120) + "..." : bid.getProposal() %>
                                        </p>
                                    </div>
                                    <div class="text-end ms-3" style="min-width: 120px;">
                                        <div class="fw-bold" style="color: #28a745;">¥<%= String.format("%.0f", bid.getAmount()) %></div>
                                        <small class="text-muted"><%= bid.getDays() %> 天</small>
                                        <% if ("pending".equals(bid.getStatus()) && isOwner && "open".equals(project.getStatus())) { %>
                                            <form action="${pageContext.request.contextPath}/bid/award" method="post" class="mt-2">
                                                <input type="hidden" name="bidId" value="<%= bid.getId() %>">
                                                <input type="hidden" name="projectId" value="<%= project.getId() %>">
                                                <button type="submit" class="btn btn-sm btn-gradient" style="padding: 4px 16px;">选标</button>
                                            </form>
                                        <% } else if ("accepted".equals(bid.getStatus())) { %>
                                            <span class="badge bg-success mt-2 d-block">已中标 ✓</span>
                                        <% } else if ("rejected".equals(bid.getStatus())) { %>
                                            <span class="badge bg-secondary mt-2 d-block">未选中</span>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else if (isOwner && "open".equals(project.getStatus())) { %>
                    <div class="card p-4 mb-4 text-center text-muted">
                        <div style="font-size: 2rem;">⏳</div>
                        <p class="mt-2">暂无竞标，请耐心等待</p>
                    </div>
                <% } %>
            </div>

            <%-- 侧边栏 --%>
            <div class="col-md-4">
                <div class="card p-4">
                    <h5 class="fw-bold mb-3">操作</h5>
                    <% if (!isOwner && "open".equals(project.getStatus())) { %>
                        <a href="${pageContext.request.contextPath}/bid/place?projectId=<%= project.getId() %>" class="btn btn-gradient w-100 mb-2">
                            📩 投递竞标
                        </a>
                    <% } %>
                    <a href="${pageContext.request.contextPath}/profile/<%= project.getEmployerId() %>" class="btn btn-outline-secondary w-100 mb-2">
                        👤 查看雇主
                    </a>
                    <% if (isOwner && "in_progress".equals(project.getStatus())) { %>
                        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-success w-100">
                            📦 查看订单
                        </a>
                    <% } %>
                    <hr>
                    <small class="text-muted d-block">项目 ID: <%= project.getId() %></small>
                    <small class="text-muted d-block">状态: 
                        <% if ("open".equals(project.getStatus())) { %>招募中<% } 
                           else if ("in_progress".equals(project.getStatus())) { %>进行中<% } 
                           else if ("completed".equals(project.getStatus())) { %>已完成<% } %>
                    </small>
                </div>
            </div>
        </div>
    </div>
    <style>
        .btn-outline-secondary { border-radius: 8px; padding: 10px; font-weight: 600; }
        .btn-outline-secondary:hover { background: #f0f0f0; }
        .btn-outline-success { border-radius: 8px; padding: 10px; font-weight: 600; border: 1.5px solid #28a745; color: #28a745; }
        .btn-outline-success:hover { background: #28a745; color: white; }
    </style>
</body>
</html>
