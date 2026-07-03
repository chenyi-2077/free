<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="chen_kai_bo.Project" %>
<%
    List<Project> projects = (List<Project>) request.getAttribute("projects");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的项目 - FreeLite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">FreeLite</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/projects">项目列表</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/project/post">发布项目</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/my/projects">我的项目</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <h3 class="mb-4">我的项目</h3>
        <%
            if (projects != null && !projects.isEmpty()) {
                for (Project p : projects) {
        %>
        <div class="card shadow-sm mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h5 class="card-title mb-1">
                            <a href="${pageContext.request.contextPath}/project/detail?id=<%= p.getId() %>" class="text-decoration-none"><%= p.getTitle() %></a>
                        </h5>
                        <p class="text-muted mb-2"><%= p.getDescription() != null && p.getDescription().length() > 150 ? p.getDescription().substring(0, 150) + "..." : p.getDescription() %></p>
                    </div>
                    <span class="badge <%= "open".equals(p.getStatus()) ? "bg-success" : "bg-secondary" %>"><%= p.getStatus() %></span>
                </div>
                <div class="d-flex flex-wrap gap-3 mt-2 text-muted small">
                    <span>💰 ¥<%= String.format("%.2f", p.getBudget()) %></span>
                    <span>📂 <%= p.getCategoryName() != null ? p.getCategoryName() : "未分类" %></span>
                    <span>📅 截止: <%= p.getDeadline() != null ? p.getDeadline().toString() : "未设置" %></span>
                </div>
                <div class="mt-2">
                    <a href="${pageContext.request.contextPath}/project/edit?id=<%= p.getId() %>" class="btn btn-sm btn-warning">编辑</a>
                    <form action="${pageContext.request.contextPath}/project/delete" method="post" class="d-inline" onsubmit="return confirm('确定要删除此项目吗？')">
                        <input type="hidden" name="id" value="<%= p.getId() %>">
                        <button type="submit" class="btn btn-sm btn-danger">删除</button>
                    </form>
                </div>
            </div>
        </div>
        <%
                }
            } else {
        %>
        <div class="alert alert-info text-center">
            你还没有发布项目
            <br>
            <a href="${pageContext.request.contextPath}/project/post" class="btn btn-gradient mt-2">发布第一个项目</a>
        </div>
        <%
            }
        %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
