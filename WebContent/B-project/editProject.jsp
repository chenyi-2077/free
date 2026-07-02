<%@ page import="java.util.List, com.freelite.model.Project, com.freelite.model.Category, com.freelite.model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    Project project = (Project) request.getAttribute("project");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑项目 - Freelite</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/bootstrap/5.3.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />

    <div class="container mt-4" style="max-width: 720px;">
        <h2 class="mb-4">编辑项目</h2>

        <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/project/edit" method="post">
            <input type="hidden" name="id" value="<%= project.getId() %>">

            <div class="mb-3">
                <label class="form-label">项目标题 <span class="text-danger">*</span></label>
                <input type="text" name="title" class="form-control" value="<%= project.getTitle() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">项目描述</label>
                <textarea name="description" class="form-control" rows="5"><%= project.getDescription() != null ? project.getDescription() : "" %></textarea>
            </div>

            <div class="row mb-3">
                <div class="col">
                    <label class="form-label">预算 (¥)</label>
                    <input type="number" name="budget" class="form-control" step="0.01" min="0" value="<%= String.format("%.2f", project.getBudget()) %>">
                </div>
                <div class="col">
                    <label class="form-label">截止日期</label>
                    <input type="date" name="deadline" class="form-control" value="<%= project.getDeadline() != null ? project.getDeadline().toString() : "" %>">
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">分类</label>
                <select name="categoryId" class="form-select">
                    <option value="">请选择分类</option>
                    <% if (categories != null) {
                        for (Category c : categories) { %>
                            <option value="<%= c.getId() %>" <%= c.getId() == project.getCategoryId() ? "selected" : "" %>><%= c.getName() %></option>
                    <%  } } %>
                </select>
            </div>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">保存修改</button>
                <a href="<%= request.getContextPath() %>/my/projects" class="btn btn-outline-secondary">取消</a>
            </div>
        </form>
    </div>

    <script src="https://cdn.bootcdn.net/ajax/libs/bootstrap/5.3.3/js/bootstrap.bundle.min.js"></script>
</body>
</html>
