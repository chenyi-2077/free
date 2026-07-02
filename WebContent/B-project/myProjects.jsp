<%@ page import="java.util.List, com.freelite.model.Project, com.freelite.model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Project> projects = (List<Project>) request.getAttribute("projects");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的项目 - Freelite</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/bootstrap/5.3.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>我的项目</h2>
            <a href="<%= request.getContextPath() %>/project/post" class="btn btn-primary">发布新项目</a>
        </div>

        <% if (projects == null || projects.isEmpty()) { %>
            <div class="alert alert-info text-center">
                你还没有发布任何项目。<br>
                <a href="<%= request.getContextPath() %>/project/post" class="alert-link">立即发布第一个项目</a>
            </div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
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
                        <% for (Project p : projects) { %>
                            <tr>
                                <td>
                                    <a href="<%= request.getContextPath() %>/project/<%= p.getId() %>" class="text-decoration-none fw-bold">
                                        <%= p.getTitle() %>
                                    </a>
                                </td>
                                <td><%= p.getCategoryName() != null ? p.getCategoryName() : "未分类" %></td>
                                <td>¥<%= String.format("%.2f", p.getBudget()) %></td>
                                <td>
                                    <% String status = p.getStatus();
                                       String badgeClass = "bg-secondary";
                                       if ("open".equals(status)) badgeClass = "bg-success";
                                       else if ("in_progress".equals(status)) badgeClass = "bg-primary";
                                       else if ("completed".equals(status)) badgeClass = "bg-info";
                                       else if ("cancelled".equals(status)) badgeClass = "bg-dark";
                                    %>
                                    <span class="badge <%= badgeClass %>">
                                        <% if ("open".equals(status)) { %>开放中
                                        <% } else if ("in_progress".equals(status)) { %>进行中
                                        <% } else if ("completed".equals(status)) { %>已完成
                                        <% } else if ("cancelled".equals(status)) { %>已取消
                                        <% } else { %><%= status %><% } %>
                                    </span>
                                </td>
                                <td><%= p.getCreatedAt() != null ? p.getCreatedAt().toLocalDate().toString() : "-" %></td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/project/<%= p.getId() %>" class="btn btn-sm btn-outline-primary">查看</a>
                                    <a href="<%= request.getContextPath() %>/project/edit?id=<%= p.getId() %>" class="btn btn-sm btn-outline-secondary">编辑</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>

    <script src="https://cdn.bootcdn.net/ajax/libs/bootstrap/5.3.3/js/bootstrap.bundle.min.js"></script>
</body>
</html>
