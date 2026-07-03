<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="chen_yi_an.User" %>
<%
    User chatUser = (User) request.getAttribute("user");
    List<Map<String, Object>> projects = (List<Map<String, Object>>) request.getAttribute("projects");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>交付沟通 - 自由人平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-8">
                <div class="card shadow-sm mb-4">
                    <div class="card-body p-4">
                        <h3 class="card-title text-center mb-4">交付沟通</h3>
                        <%
                            if (chatUser == null) {
                        %>
                        <p class="text-muted text-center">请先登录</p>
                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-gradient">去登录</a>
                        </div>
                        <%
                            } else {
                        %>
                        <p class="text-muted text-center mb-4">您好，<%= chatUser.getDisplayName() != null ? chatUser.getDisplayName() : chatUser.getEmail() %></p>
                        <%
                            if (projects != null && !projects.isEmpty()) {
                                for (Map<String, Object> project : projects) {
                                    int progress = (int) project.get("progress");
                        %>
                        <div class="list-group mb-4">
                            <div class="list-group-item">
                                <div class="d-flex w-100 justify-content-between">
                                    <h6 class="mb-1"><%= project.get("title") %></h6>
                                    <small class="text-muted"><%= project.get("status") %></small>
                                </div>
                                <p class="mb-1 text-muted small"><%= project.get("description") != null ? project.get("description") : "" %></p>
                                <div class="progress mt-2" style="height: 8px;">
                                    <div class="progress-bar" role="progressbar" style="width: <%= progress %>%" aria-valuenow="<%= progress %>" aria-valuemin="0" aria-valuemax="100"></div>
                                </div>
                                <small class="text-muted">进度：<%= progress %>%</small>
                            </div>
                        </div>
                        <%
                                }
                            } else {
                        %>
                        <p class="text-muted text-center">暂无参与的项目</p>
                        <%
                            }
                        %>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
