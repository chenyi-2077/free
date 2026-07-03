<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.freelite.model.Project, com.freelite.model.User" %>
<%
    User mpUser = (User) session.getAttribute("user");
    if (mpUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<Project> mpList = (List<Project>) request.getAttribute("projects");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的项目 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />
    <div class="container py-4">
        <h4 class="mb-4">我的项目</h4>
        <a href="${pageContext.request.contextPath}/project/post" class="btn btn-primary mb-3">发布新项目</a>
        <div class="row">
            <% if (mpList != null) for (Project p : mpList) { %>
                <div class="col-md-6 mb-3">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h5><a href="${pageContext.request.contextPath}/project/detail?id=<%= p.getId() %>"><%= p.getTitle() %></a></h5>
                            <p class="text-muted small">预算：¥<%= String.format("%.0f", p.getBudget()) %> | 状态：<%= p.getStatus() %></p>
                            <a href="${pageContext.request.contextPath}/project/edit?id=<%= p.getId() %>" class="btn btn-outline-primary btn-sm">编辑</a>
                            <form method="post" action="${pageContext.request.contextPath}/project/delete" style="display:inline">
                                <input type="hidden" name="id" value="<%= p.getId() %>">
                                <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('确定删除？')">删除</button>
                            </form>
                        </div>
                    </div>
                </div>
            <% } %>
            <% if (mpList == null || mpList.isEmpty()) { %>
                <div class="col-12"><p class="text-muted">您还没有发布项目</p></div>
            <% } %>
        </div>
    </div>
</body>
</html>
