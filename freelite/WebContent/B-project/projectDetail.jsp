<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freelite.model.Project, com.freelite.model.User" %>
<%
    Project pd = (Project) request.getAttribute("project");
    if (pd == null) { response.sendRedirect(request.getContextPath() + "/projects"); return; }
    User pdUser = (User) session.getAttribute("user");
    boolean isOwner = pdUser != null && pdUser.getId() == pd.getEmployerId();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= pd.getTitle() %> - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/WEB-INF/tags/navbar.jsp" %>
    <div class="container py-4">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h4><%= pd.getTitle() %></h4>
                <span class="badge <%= "open".equals(pd.getStatus()) ? "bg-success" : "bg-secondary" %>"><%= pd.getStatus() %></span>
                <hr>
                <p><%= pd.getDescription() != null ? pd.getDescription() : "暂无描述" %></p>
                <div class="row">
                    <div class="col-md-3"><strong>预算：</strong>¥<%= String.format("%.2f", pd.getBudget()) %></div>
                    <div class="col-md-3"><strong>分类：</strong><%= pd.getCategoryName() != null ? pd.getCategoryName() : "未分类" %></div>
                    <div class="col-md-3"><strong>发布者：</strong><%= pd.getEmployerName() %></div>
                    <div class="col-md-3"><strong>截止：</strong><%= pd.getDeadline() != null ? pd.getDeadline().toString() : "无" %></div>
                </div>
                <% if (isOwner) { %>
                    <hr>
                    <a href="${pageContext.request.contextPath}/project/edit?id=<%= pd.getId() %>" class="btn btn-outline-primary btn-sm">编辑</a>
                    <form method="post" action="${pageContext.request.contextPath}/project/delete" style="display:inline">
                        <input type="hidden" name="id" value="<%= pd.getId() %>">
                        <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('确定删除？')">删除</button>
                    </form>
                    <% if ("open".equals(pd.getStatus())) { %>
                        <form method="post" action="${pageContext.request.contextPath}/project/status" style="display:inline">
                            <input type="hidden" name="id" value="<%= pd.getId() %>">
                            <input type="hidden" name="status" value="cancelled">
                            <button type="submit" class="btn btn-outline-warning btn-sm">关闭项目</button>
                        </form>
                    <% } %>
                    <% if ("cancelled".equals(pd.getStatus())) { %>
                        <form method="post" action="${pageContext.request.contextPath}/project/status" style="display:inline">
                            <input type="hidden" name="id" value="<%= pd.getId() %>">
                            <input type="hidden" name="status" value="open">
                            <button type="submit" class="btn btn-outline-success btn-sm">重新开放</button>
                        </form>
                    <% } %>
                <% } %>
                <hr>
                <h5 class="mt-3">竞标</h5>
                <a href="${pageContext.request.contextPath}/bids?projectId=<%= pd.getId() %>" class="btn btn-outline-primary btn-sm">查看竞标</a>
                <a href="${pageContext.request.contextPath}/bid/place?projectId=<%= pd.getId() %>" class="btn btn-primary btn-sm">提交竞标</a>
            </div>
        </div>
    </div>
</body>
</html>
