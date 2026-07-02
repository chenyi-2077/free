<%@ page import="java.util.List, com.freelite.model.Project, com.freelite.model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Project> myProjects = (List<Project>) request.getAttribute("myProjects");
    List<Project> biddedProjects = (List<Project>) request.getAttribute("biddedProjects");
    String activeTab = request.getParameter("tab");
    if (activeTab == null) activeTab = "my";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的项目 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        .nav-tabs .nav-link {
            color: #6b7280;
            border: none;
            padding: 10px 20px;
            font-weight: 500;
        }
        .nav-tabs .nav-link:hover {
            color: #111827;
            border: none;
        }
        .nav-tabs .nav-link.active {
            color: #00C897;
            background: none;
            border: none;
            border-bottom: 2px solid #00C897;
        }
        .nav-tabs {
            border-bottom: 1px solid #e5e7eb;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="fw-bold mb-0">📁 我的项目</h4>
            <a href="${pageContext.request.contextPath}/project/post" class="btn btn-gradient">
                <i class="bi bi-plus-lg"></i> 发布新项目
            </a>
        </div>

        <!-- Tab 导航 -->
        <ul class="nav nav-tabs mb-3">
            <li class="nav-item">
                <a class="nav-link <%= "my".equals(activeTab) ? "active" : "" %>" 
                   href="?tab=my">我开发的项目</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <%= "bidded".equals(activeTab) ? "active" : "" %>" 
                   href="?tab=bidded">我竞标的项目</a>
            </li>
        </ul>

        <% if ("my".equals(activeTab)) { %>
            <!-- 我开发的项目 -->
            <% if (myProjects == null || myProjects.isEmpty()) { %>
                <div class="card p-5 text-center">
                    <div style="font-size: 3rem;">📋</div>
                    <p class="text-muted mt-3 mb-3">你还没有发布过项目</p>
                    <a href="${pageContext.request.contextPath}/project/post" class="btn btn-gradient" style="width: auto; align-self: center;">发布第一个项目</a>
                </div>
            <% } else { %>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead style="background: var(--okx-bg-secondary);">
                            <tr>
                                <th>标题</th>
                                <th>分类</th>
                                <th>预算</th>
                                <th>状态</th>
                                <th>发布日期</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Project p : myProjects) { %>
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/project/<%= p.getId() %>" class="text-decoration-none fw-bold" style="color: #111827;">
                                            <%= p.getTitle() %>
                                        </a>
                                    </td>
                                    <td><%= p.getCategoryName() != null ? p.getCategoryName() : "未分类" %></td>
                                    <td style="color: #00C897; font-weight: 600;">¥<%= String.format("%.2f", p.getBudget()) %></td>
                                    <td>
                                        <% String status = p.getStatus();
                                           String badgeClass = "bg-secondary";
                                           if ("open".equals(status)) badgeClass = "bg-success";
                                           else if ("in_progress".equals(status)) badgeClass = "bg-primary";
                                           else if ("completed".equals(status)) badgeClass = "bg-info";
                                           else if ("cancelled".equals(status)) badgeClass = "bg-secondary";
                                        %>
                                        <span class="badge <%= badgeClass %>">
                                            <% if ("open".equals(status)) { %>开放中
                                            <% } else if ("in_progress".equals(status)) { %>进行中
                                            <% } else if ("completed".equals(status)) { %>已完成
                                            <% } else if ("cancelled".equals(status)) { %>已取消
                                            <% } else { %><%= status %><% } %>
                                        </span>
                                    </td>
                                    <td style="color: #6b7280;"><%= p.getCreatedAt() != null ? p.getCreatedAt().toLocalDate().toString() : "-" %></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/project/<%= p.getId() %>" class="btn btn-sm btn-outline-light">查看</a>
                                        <a href="${pageContext.request.contextPath}/project/edit?id=<%= p.getId() %>" class="btn btn-sm btn-outline-light">编辑</a>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        <% } else { %>
            <!-- 我竞标的项目 -->
            <% if (biddedProjects == null || biddedProjects.isEmpty()) { %>
                <div class="card p-5 text-center">
                    <div style="font-size: 3rem;">📩</div>
                    <p class="text-muted mt-3 mb-3">还没有竞标过任何项目</p>
                    <a href="${pageContext.request.contextPath}/projects" class="btn btn-gradient" style="width: auto; align-self: center;">去看看项目</a>
                </div>
            <% } else { %>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead style="background: var(--okx-bg-secondary);">
                            <tr>
                                <th>项目标题</th>
                                <th>雇主</th>
                                <th>预算</th>
                                <th>状态</th>
                                <th>发布日期</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Project p : biddedProjects) { %>
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/bid/place?projectId=<%= p.getId() %>" class="text-decoration-none fw-bold" style="color: #111827;">
                                            <%= p.getTitle() %>
                                        </a>
                                    </td>
                                    <td style="color: #6b7280;"><%= p.getEmployerName() != null ? p.getEmployerName() : "未知" %></td>
                                    <td style="color: #00C897; font-weight: 600;">¥<%= String.format("%.2f", p.getBudget()) %></td>
                                    <td>
                                        <% String status = p.getStatus();
                                           String badgeClass = "bg-secondary";
                                           if ("open".equals(status)) badgeClass = "bg-success";
                                           else if ("in_progress".equals(status)) badgeClass = "bg-primary";
                                           else if ("completed".equals(status)) badgeClass = "bg-info";
                                           else if ("cancelled".equals(status)) badgeClass = "bg-secondary";
                                        %>
                                        <span class="badge <%= badgeClass %>">
                                            <% if ("open".equals(status)) { %>开放中
                                            <% } else if ("in_progress".equals(status)) { %>进行中
                                            <% } else if ("completed".equals(status)) { %>已完成
                                            <% } else if ("cancelled".equals(status)) { %>已取消
                                            <% } else { %><%= status %><% } %>
                                        </span>
                                    </td>
                                    <td style="color: #6b7280;"><%= p.getCreatedAt() != null ? p.getCreatedAt().toLocalDate().toString() : "-" %></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/bid/place?projectId=<%= p.getId() %>" class="btn btn-sm btn-outline-light">查看竞标</a>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
