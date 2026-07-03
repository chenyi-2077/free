<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.freelite.model.Category, com.freelite.model.User" %>
<%
    User pu = (User) session.getAttribute("user");
    if (pu == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<Category> cats = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>发布项目 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/WEB-INF/tags/navbar.jsp" %>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h4>发布项目</h4>
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <form method="post">
                            <div class="mb-3">
                                <label class="form-label">项目标题</label>
                                <input type="text" name="title" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">项目描述</label>
                                <textarea name="description" class="form-control" rows="5"></textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">预算 (¥)</label>
                                    <input type="number" name="budget" class="form-control" step="0.01" min="0">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">截止日期</label>
                                    <input type="date" name="deadline" class="form-control">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">分类</label>
                                    <select name="categoryId" class="form-select">
                                        <% if (cats != null) for (Category c : cats) { %>
                                            <option value="<%= c.getId() %>"><%= c.getName() %></option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">发布</button>
                            <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary">取消</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
