<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.freelite.model.Project, com.freelite.model.Category" %>
<%
    List<Project> projects = (List<Project>) request.getAttribute("projects");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String keyword = request.getParameter("keyword");
    String selCat = request.getParameter("categoryId");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>项目列表 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/WEB-INF/tags/navbar.jsp" %>
    <div class="container py-4">
        <h4 class="mb-4">项目市场</h4>
        <form class="row g-2 mb-4" method="get">
            <div class="col-md-5">
                <input type="text" name="keyword" class="form-control" placeholder="搜索项目标题/描述" value="<%= keyword != null ? keyword : "" %>">
            </div>
            <div class="col-md-3">
                <select name="categoryId" class="form-select">
                    <option value="">全部分类</option>
                    <% if (categories != null) for (Category c : categories) { %>
                        <option value="<%= c.getId() %>" <%= String.valueOf(c.getId()).equals(selCat) ? "selected" : "" %>><%= c.getName() %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary">搜索</button>
            </div>
        </form>
        <div class="row">
            <% if (projects != null) for (Project p : projects) { %>
                <div class="col-md-6 mb-3">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h5><a href="${pageContext.request.contextPath}/project/detail?id=<%= p.getId() %>" class="text-decoration-none"><%= p.getTitle() %></a></h5>
                            <p class="text-muted small"><%= p.getDescription() != null && p.getDescription().length() > 100 ? p.getDescription().substring(0, 100) + "..." : p.getDescription() %></p>
                            <div class="d-flex justify-content-between">
                                <span class="badge bg-primary">¥<%= String.format("%.0f", p.getBudget()) %></span>
                                <span class="badge bg-secondary"><%= p.getCategoryName() != null ? p.getCategoryName() : "未分类" %></span>
                                <span class="badge <%= "open".equals(p.getStatus()) ? "bg-success" : "bg-secondary" %>"><%= p.getStatus() %></span>
                            </div>
                            <small class="text-muted">发布者：<%= p.getEmployerName() %></small>
                        </div>
                    </div>
                </div>
            <% } %>
            <% if (projects == null || projects.isEmpty()) { %>
                <div class="col-12"><p class="text-muted">暂无项目</p></div>
            <% } %>
        </div>
    </div>
</body>
</html>
