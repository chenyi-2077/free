<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="chen_kai_bo.Project" %>
<%@ page import="chen_kai_bo.User" %>
<%
    Project project = (Project) request.getAttribute("project");
    Boolean isOwner = (Boolean) request.getAttribute("isOwner");
    User currentUser = (User) request.getAttribute("currentUser");
    if (project == null) {
        response.sendRedirect(request.getContextPath() + "/projects");
        return;
    }
    if (isOwner == null) isOwner = false;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= project.getTitle() %> - FreeLite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/my/projects">我的项目</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h4 class="mb-0"><%= project.getTitle() %></h4>
                        <span class="badge <%= "open".equals(project.getStatus()) ? "bg-success" : "bg-secondary" %> fs-6"><%= project.getStatus() %></span>
                    </div>
                    <div class="card-body">
                        <div class="mb-4">
                            <h6 class="text-muted">项目描述</h6>
                            <p class="mb-0" style="white-space: pre-wrap;"><%= project.getDescription() != null ? project.getDescription() : "暂无描述" %></p>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6 mb-2">
                                <div class="border rounded p-3 bg-white">
                                    <small class="text-muted d-block">预算</small>
                                    <strong>¥ <%= String.format("%.2f", project.getBudget()) %></strong>
                                </div>
                            </div>
                            <div class="col-md-6 mb-2">
                                <div class="border rounded p-3 bg-white">
                                    <small class="text-muted d-block">截止日期</small>
                                    <strong><%= project.getDeadline() != null ? project.getDeadline().toString() : "未设置" %></strong>
                                </div>
                            </div>
                            <div class="col-md-6 mb-2">
                                <div class="border rounded p-3 bg-white">
                                    <small class="text-muted d-block">分类</small>
                                    <strong><%= project.getCategoryName() != null ? project.getCategoryName() : "未分类" %></strong>
                                </div>
                            </div>
                            <div class="col-md-6 mb-2">
                                <div class="border rounded p-3 bg-white">
                                    <small class="text-muted d-block">雇主</small>
                                    <strong><%= project.getEmployerName() != null ? project.getEmployerName() : "未知" %></strong>
                                </div>
                            </div>
                        </div>

                        <% if (isOwner) { %>
                        <hr>
                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/project/edit?id=<%= project.getId() %>" class="btn btn-warning">编辑项目</a>
                            <form action="${pageContext.request.contextPath}/project/status" method="post" class="d-inline">
                                <input type="hidden" name="id" value="<%= project.getId() %>">
                                <%
                                    String nextStatus = "open".equals(project.getStatus()) ? "closed" : "open";
                                    String nextLabel = "open".equals(project.getStatus()) ? "关闭项目" : "重新开启";
                                    String btnClass = "open".equals(project.getStatus()) ? "btn-secondary" : "btn-success";
                                %>
                                <input type="hidden" name="status" value="<%= nextStatus %>">
                                <button type="submit" class="btn <%= btnClass %>"><%= nextLabel %></button>
                            </form>
                            <form action="${pageContext.request.contextPath}/project/delete" method="post" class="d-inline" onsubmit="return confirm('确定要删除此项目吗？')">
                                <input type="hidden" name="id" value="<%= project.getId() %>">
                                <button type="submit" class="btn btn-danger">删除项目</button>
                            </form>
                        </div>
                        <% } %>

                        <div class="mt-3">
                            <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary">← 返回列表</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
