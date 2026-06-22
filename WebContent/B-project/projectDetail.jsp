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
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .budget-big { font-size: 2rem; font-weight: 700; color: #28a745; }
        .bid-card { border-left: 4px solid #667eea; transition: all 0.2s; }
        .bid-card:hover { background: #f8f9ff; }
        .btn-primary {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            border-radius: 8px;
            padding: 10px 24px;
            font-weight: 600;
        }
        .btn-outline-primary {
            border-color: #667eea;
            color: #667eea;
            border-radius: 8px;
        }
        .btn-outline-primary:hover { background: #667eea; color: white; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: #667eea;">Freelite</a>
            <div class="d-flex align-items-center gap-3">
                <a href="<%= request.getContextPath() %>/projects" class="text-decoration-none text-muted">← 返回列表</a>
                <a href="<%= request.getContextPath() %>/profile" class="text-decoration-none text-muted"><%= loginUser.getDisplayName() %></a>
                <a href="<%= request.getContextPath() %>/logout" class="text-decoration-none text-muted">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-md-8">

                <%-- 项目详情 --%>
                <div class="card p-4 mb-4">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h3 class="fw-bold"><%= project.getTitle() %></h3>
                            <p>
                                <span class="badge bg-light text-dark"><%= project.getCategoryName() %></span>
                                <span class="text-muted ms-2">发布者：<%= project.getEmployerName() %></span>
                                <span class="text-muted ms-2"><%= project.getCreatedAt() != null ? project.getCreatedAt().substring(0, 10) : "" %></span>
                                <% if ("open".equals(project.getStatus())) { %>
                                    <span class="badge bg-success ms-2">招募中</span>
                                <% } else if ("in_progress".equals(project.getStatus())) { %>
                                    <span class="badge bg-warning text-dark ms-2">进行中</span>
                                <% } else if ("completed".equals(project.getStatus())) { %>
                                    <span class="badge bg-secondary ms-2">已完成</span>
                                <% } %>
                            </p>
                        </div>
                        <div class="text-end">
                            <div class="budget-big">¥<%= String.format("%.0f", project.getBudget()) %></div>
                            <small class="text-muted">预算</small>
                            <div class="mt-1"><small class="text-muted">截止：<%= project.getDeadline() != null ? project.getDeadline() : "未设置" %></small></div>
                        </div>
                    </div>
                    <hr>
                    <p style="white-space: pre-wrap;"><%= project.getDescription() != null ? project.getDescription() : "暂无描述" %></p>
                </div>

                <%-- 竞标列表（雇主视角） --%>
                <% if (isOwner && bids != null && !bids.isEmpty()) { %>
                    <div class="card p-4 mb-4">
                        <h5 class="fw-bold mb-3">📩 收到的竞标（<%= bids.size() %>）</h5>
                        <% for (Bid bid : bids) { %>
                            <div class="card bid-card p-3 mb-2">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <strong><%= bid.getFreelancerName() %></strong>
                                        <span class="badge bg-warning text-dark ms-2"><%= bid.getFreelancerRating() %></span>
                                        <p class="text-muted small mt-1 mb-0"><%= bid.getProposal() != null && bid.getProposal().length() > 100 ? bid.getProposal().substring(0, 100) + "..." : bid.getProposal() %></p>
                                    </div>
                                    <div class="text-end">
                                        <div class="fw-bold" style="color: #28a745;">¥<%= String.format("%.0f", bid.getAmount()) %></div>
                                        <small class="text-muted"><%= bid.getDays() %> 天</small>
                                        <% if ("pending".equals(bid.getStatus())) { %>
                                            <form action="<%= request.getContextPath() %>/bid/award" method="post" class="mt-2">
                                                <input type="hidden" name="bidId" value="<%= bid.getId() %>">
                                                <input type="hidden" name="projectId" value="<%= project.getId() %>">
                                                <button type="submit" class="btn btn-sm btn-primary">选标</button>
                                            </form>
                                        <% } else if ("accepted".equals(bid.getStatus())) { %>
                                            <span class="badge bg-success">已中标</span>
                                        <% } else { %>
                                            <span class="badge bg-secondary">未选中</span>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>

            </div>

            <%-- 侧边栏 --%>
            <div class="col-md-4">
                <div class="card p-4">
                    <h5 class="fw-bold mb-3">操作</h5>
                    <% if (!isOwner && "open".equals(project.getStatus())) { %>
                        <a href="<%= request.getContextPath() %>/bid/place?projectId=<%= project.getId() %>" class="btn btn-primary w-100 mb-2">📩 投递竞标</a>
                    <% } %>
                    <a href="<%= request.getContextPath() %>/profile/<%= project.getEmployerId() %>" class="btn btn-outline-primary w-100 mb-2">👤 查看雇主</a>
                    <hr>
                    <small class="text-muted">项目ID: <%= project.getId() %></small><br>
                    <small class="text-muted">状态: <%= project.getStatus() %></small>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
