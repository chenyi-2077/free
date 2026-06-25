<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    List<Project> projects = (List<Project>) request.getAttribute("projects");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalPages = (int) request.getAttribute("totalPages");
    String keyword = (String) request.getAttribute("keyword");
    int selectedCategory = (int) request.getAttribute("selectedCategory");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>项目市场 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        .card-project {
            transition: all 0.2s;
            cursor: pointer;
            height: 100%;
        }
        .card-project:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }
        a.card-project-link { text-decoration: none; color: inherit; display: block; }
        .page-link { color: #667eea; }
        .page-item.active .page-link { background: #667eea; border-color: #667eea; }
    </style>
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <div class="d-flex gap-3 align-items-center">
                <span class="text-muted" style="font-size: 0.9rem;">
                    <%= loginUser.getDisplayName() != null ? loginUser.getDisplayName() : loginUser.getEmail() %>
                </span>
                <a href="${pageContext.request.contextPath}/my/projects" class="nav-link">我的项目</a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-link">个人主页</a>
                <a href="${pageContext.request.contextPath}/orders" class="nav-link">订单</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">看板</a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <%-- 头部 --%>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="fw-bold mb-0">📋 项目市场</h4>
            <a href="${pageContext.request.contextPath}/project/post" class="btn btn-gradient">
                <i class="bi bi-plus-lg"></i> 发布项目
            </a>
        </div>

        <%-- 搜索 --%>
        <form action="${pageContext.request.contextPath}/projects" method="get" class="mb-4">
            <div class="row g-2">
                <div class="col-md-6">
                    <div class="input-group">
                        <span class="input-group-text bg-white border-end-0"><i class="bi bi-search text-muted"></i></span>
                        <input type="text" name="keyword" class="form-control border-start-0" placeholder="搜索项目..."
                               value="<%= keyword != null ? keyword : "" %>">
                    </div>
                </div>
                <div class="col-md-3">
                    <select name="category" class="form-select" onchange="this.form.submit()">
                        <option value="0">全部分类</option>
                        <% for (Category c : categories) { %>
                            <option value="<%= c.getId() %>" <%= c.getId() == selectedCategory ? "selected" : "" %>><%= c.getName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-gradient w-100">搜索</button>
                </div>
            </div>
        </form>

        <%-- 项目卡片列表 --%>
        <div class="row">
            <% if (projects == null || projects.isEmpty()) { %>
                <div class="col-12 text-center py-5">
                    <div style="font-size: 3rem;">🔍</div>
                    <p class="text-muted mt-3">暂无项目</p>
                    <a href="${pageContext.request.contextPath}/project/post" class="btn btn-gradient">发布第一个项目</a>
                </div>
            <% } else { %>
                <% for (Project p : projects) { %>
                    <div class="col-md-6 mb-3">
                        <a href="${pageContext.request.contextPath}/project/<%= p.getId() %>" class="card-project-link">
                            <div class="card card-project p-3">
                                <div class="d-flex justify-content-between">
                                    <div style="flex: 1;">
                                        <h5 class="fw-bold mb-1"><%= p.getTitle() %></h5>
                                        <div class="mb-2">
                                            <span class="skill-badge"><%= p.getCategoryName() %></span>
                                            <span class="text-muted ms-2" style="font-size: 0.85rem;"><%= p.getEmployerName() %></span>
                                        </div>
                                        <p class="text-muted mb-0" style="font-size: 0.9rem;">
                                            <%= p.getDescription() != null && p.getDescription().length() > 120 ?
                                                p.getDescription().substring(0, 120) + "..." : p.getDescription() %>
                                        </p>
                                    </div>
                                    <div class="text-end ms-3" style="min-width: 100px;">
                                        <div class="price-tag">¥<%= String.format("%.0f", p.getBudget()) %></div>
                                        <div style="font-size: 0.8rem; color: #999;">
                                            <% java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"); %><%= p.getCreatedAt() != null ? p.getCreatedAt().format(dtf) : "" %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                <% } %>
            <% } %>
        </div>

        <%-- 分页 --%>
        <% if (totalPages > 1) { %>
            <nav class="mt-3">
                <ul class="pagination justify-content-center">
                    <% for (int i = 1; i <= totalPages; i++) { %>
                        <li class="page-item <%= i == currentPage ? "active" : "" %>">
                            <a class="page-link" href="${pageContext.request.contextPath}/projects?page=<%= i %>
                                <%= keyword != null && !keyword.isEmpty() ? "&keyword=" + java.net.URLEncoder.encode(keyword, "UTF-8") : "" %>
                                <%= selectedCategory > 0 ? "&category=" + selectedCategory : "" %>">
                                <%= i %>
                            </a>
                        </li>
                    <% } %>
                </ul>
            </nav>
        <% } %>
    </div>
</body>
</html>
