<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freelite.model.Delivery, com.freelite.model.ProjectMessage, com.freelite.model.User, java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>交付沟通 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/projects">项目列表</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/project/post">发布项目</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/my/projects">我的项目</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bids">竞标</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/my/bids">我的竞标</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/orders">订单</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">数据看板</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/wallet">钱包</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">登录</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/register">注册</a></li>
            </ul>
        </div>
    </div>
</nav>

<%
    Integer projectId = (Integer) request.getAttribute("projectId");
    List<Delivery> deliveries = (List<Delivery>) request.getAttribute("deliveries");
    List<ProjectMessage> messages = (List<ProjectMessage>) request.getAttribute("messages");
    User chatUser = (User) session.getAttribute("user");
%>

<div class="container mt-4">
    <div class="row">
        <div class="col-12">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/projects">项目列表</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/project/detail?id=<%= projectId %>">项目 #<%= projectId %></a></li>
                    <li class="breadcrumb-item active">交付沟通</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header"><h5>留言</h5></div>
                <div class="card-body" style="max-height: 400px; overflow-y: auto;">
                    <% if (messages != null && !messages.isEmpty()) { %>
                        <% for (ProjectMessage msg : messages) { %>
                            <div class="border-bottom pb-2 mb-2">
                                <strong><%= msg.getSenderName() != null ? msg.getSenderName() : "匿名" %></strong>
                                <small class="text-muted float-end"><%= msg.getCreatedAt() != null ? msg.getCreatedAt().toString().substring(0, 16) : "" %></small>
                                <p class="mb-0"><%= msg.getContent() %></p>
                            </div>
                        <% } %>
                    <% } else { %>
                        <p class="text-muted">暂无留言</p>
                    <% } %>
                </div>
                <% if (chatUser != null) { %>
                <div class="card-footer">
                    <form method="post" action="${pageContext.request.contextPath}/deliveryChat">
                        <input type="hidden" name="action" value="message">
                        <input type="hidden" name="projectId" value="<%= projectId %>">
                        <div class="input-group">
                            <textarea name="content" class="form-control" rows="2" placeholder="输入留言..."></textarea>
                            <button type="submit" class="btn btn-primary">发送</button>
                        </div>
                    </form>
                </div>
                <% } %>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header"><h5>交付内容</h5></div>
                <div class="card-body" style="max-height: 400px; overflow-y: auto;">
                    <% if (deliveries != null && !deliveries.isEmpty()) { %>
                        <% for (Delivery d : deliveries) { %>
                            <div class="border-bottom pb-2 mb-2">
                                <strong><%= d.getSenderName() != null ? d.getSenderName() : "匿名" %></strong>
                                <small class="text-muted float-end"><%= d.getCreatedAt() != null ? d.getCreatedAt().toString().substring(0, 16) : "" %></small>
                                <p class="mb-0"><%= d.getContent() %></p>
                                <% if (d.getFileName() != null && !d.getFileName().isEmpty()) { %>
                                    <small class="text-info"><i class="bi bi-paperclip"></i> <%= d.getFileName() %></small>
                                <% } %>
                            </div>
                        <% } %>
                    <% } else { %>
                        <p class="text-muted">暂无交付内容</p>
                    <% } %>
                </div>
                <% if (chatUser != null) { %>
                <div class="card-footer">
                    <form method="post" action="${pageContext.request.contextPath}/deliveryChat">
                        <input type="hidden" name="action" value="delivery">
                        <input type="hidden" name="projectId" value="<%= projectId %>">
                        <div class="mb-2">
                            <textarea name="content" class="form-control" rows="2" placeholder="交付说明..."></textarea>
                        </div>
                        <div class="input-group">
                            <input type="text" name="fileName" class="form-control" placeholder="文件名">
                            <input type="text" name="filePath" class="form-control" placeholder="文件路径">
                            <button type="submit" class="btn btn-success">交付</button>
                        </div>
                    </form>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
