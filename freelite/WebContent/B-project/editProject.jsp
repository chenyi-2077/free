<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freelite.model.Project, com.freelite.model.Category, java.util.List, com.freelite.model.User" %>
<%
    User eu = (User) session.getAttribute("user");
    if (eu == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    Project ep = (Project) request.getAttribute("project");
    List<Category> ecats = (List<Category>) request.getAttribute("categories");
    if (ep == null) { response.sendRedirect(request.getContextPath() + "/my/projects"); return; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>编辑项目 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/WEB-INF/tags/navbar.jsp" %>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h4>编辑项目</h4>
                        <form method="post">
                            <input type="hidden" name="id" value="<%= ep.getId() %>">
                            <div class="mb-3">
                                <label class="form-label">标题</label>
                                <input type="text" name="title" class="form-control" value="<%= ep.getTitle() %>" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">描述</label>
                                <textarea name="description" class="form-control" rows="5"><%= ep.getDescription() != null ? ep.getDescription() : "" %></textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">预算</label>
                                    <input type="number" name="budget" class="form-control" step="0.01" value="<%= ep.getBudget() %>">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">截止日期</label>
                                    <input type="date" name="deadline" class="form-control" value="<%= ep.getDeadline() != null ? ep.getDeadline().toString() : "" %>">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">分类</label>
                                    <select name="categoryId" class="form-select">
                                        <% if (ecats != null) for (Category c : ecats) { %>
                                            <option value="<%= c.getId() %>" <%= c.getId() == ep.getCategoryId() ? "selected" : "" %>><%= c.getName() %></option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">保存</button>
                            <a href="${pageContext.request.contextPath}/project/detail?id=<%= ep.getId() %>" class="btn btn-outline-secondary">取消</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
