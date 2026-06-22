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
    <title>项目列表 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .project-card {
            background: white;
            border-radius: 12px;
            border: none;
            transition: all 0.2s;
            cursor: pointer;
        }
        .project-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }
        .badge-category {
            background: rgba(102, 126, 234, 0.12);
            color: #667eea;
            border-radius: 20px;
            padding: 3px 10px;
            font-size: 0.8rem;
        }
        .budget-tag {
            font-size: 1.1rem;
            font-weight: 700;
            color: #28a745;
        }
        .page-link { color: #667eea; }
        .page-item.active .page-link {
            background: #667eea;
            border-color: #667eea;
        }
        .btn-post {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border: none;
            border-radius: 8px;
            padding: 8px 20px;
            font-weight: 600;
        }
    </style>
</head>
<body>

    <%-- 导航栏 --%>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#" style="color: #667eea;">Freelite</a>
            <div class="d-flex align-items-center gap-3">
                <span class="text-muted">你好, <%= loginUser.getDisplayName() != null ? loginUser.getDisplayName() : loginUser.getEmail() %></span>
                <a href="<%= request.getContextPath() %>/profile" class="text-decoration-none text-muted">个人主页</a>
                <a href="<%= request.getContextPath() %>/dashboard" class="text-decoration-none text-muted">看板</a>
                <a href="<%= request.getContextPath() %>/logout" class="text-decoration-none text-muted">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">

        <%-- 顶部：搜索 + 发布按钮 --%>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="fw-bold mb-0">项目市场</h4>
            <a href="<%= request.getContextPath() %>/project/post" class="btn btn-post">
                <i class="bi bi-plus-lg"></i> 发布项目
            </a>
        </div>

        <%-- 搜索栏 --%>
        <form action="<%= request.getContextPath() %>/projects" method="get" class="mb-4">
            <div class="row g-2">
                <div class="col-md-6">
                    <div class="input-group">
                        <span class="input-group-text bg-white"><i class="bi bi-search"></i></span>
                        <input type="text" name="keyword" class="form-control" placeholder="搜索项目标题或描述..."
                               value="<%= keyword != null ? keyword : "" %>">
                    </div>
                </div>
                <div class="col-md-3">
                    <select name="category" class="form-select">
                        <option value="0">全部分类</option>
                        <% for (Category c : categories) { %>
                            <option value="<%= c.getId() %>" <%= c.getId() == selectedCategory ? "selected" : "" %>>
                                <%= c.getName() %>
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-outline-primary w-100">搜索</button>
                </div>
            </div>
        </form>

        <%-- 项目列表 --%>
        <div class="row">
            <% if (projects == null || projects.isEmpty()) { %>
                <div class="col-12 text-center py-5">
                    <div style="font-size: 3rem;">🔍</div>
                    <p class="text-muted mt-3">暂无项目</p>
                </div>
            <% } else { %>
                <% for (Project p : projects) { %>
                    <div class="col-md-6 mb-3">
                        <a href="<%= request.getContextPath() %>/project/<%= p.getId() %>" class="text-decoration-none">
                            <div class="card project-card p-3">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h5 class="fw-bold text-dark mb-1"><%= p.getTitle() %></h5>
                                        <span class="badge-category"><%= p.getCategoryName() %></span>
                                        <span class="text-muted ms-2 small"><%= p.getEmployerName() %></span>
                                        <p class="text-muted small mt-2 mb-0">
                                            <%= p.getDescription() != null && p.getDescription().length() > 100 ?
                                                p.getDescription().substring(0, 100) + "..." : p.getDescription() %>
                                        </p>
                                    </div>
                                    <div class="text-end">
                                        <div class="budget-tag">¥<%= String.format("%.0f", p.getBudget()) %></div>
                                        <small class="text-muted"><%= p.getCreatedAt() != null ? p.getCreatedAt().substring(0, 10) : "" %></small>
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
            <nav class="mt-4">
                <ul class="pagination justify-content-center">
                    <% for (int i = 1; i <= totalPages; i++) { %>
                        <li class="page-item <%= i == currentPage ? "active" : "" %>">
                            <a class="page-link" href="<%= request.getContextPath() %>/projects?page=<%= i %>
                                <%= keyword != null && !keyword.isEmpty() ? "&keyword=" + keyword : "" %>
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
