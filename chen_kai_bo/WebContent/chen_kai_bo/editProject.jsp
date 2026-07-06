<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="chen_kai_bo.Project" %>
<%@ page import="chen_kai_bo.Category" %>
<%
    Project project = (Project) request.getAttribute("project");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    if (project == null) {
        response.sendRedirect(request.getContextPath() + "/my/projects");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>编辑项目 - FreeLite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>

    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h4 class="mb-0">编辑项目</h4>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/project/edit" method="post">
                            <input type="hidden" name="id" value="<%= project.getId() %>">
                            <div class="mb-3">
                                <label for="title" class="form-label">项目标题 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" value="<%= project.getTitle() != null ? project.getTitle() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">项目描述 <span class="text-danger">*</span></label>
                                <textarea class="form-control" id="description" name="description" rows="5" required><%= project.getDescription() != null ? project.getDescription() : "" %></textarea>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="budget" class="form-label">预算 (¥)</label>
                                    <input type="number" step="0.01" min="0" class="form-control" id="budget" name="budget" value="<%= project.getBudget() > 0 ? String.format("%.2f", project.getBudget()) : "" %>">
                                </div>
                                <div class="col-md-6">
                                    <label for="deadline" class="form-label">截止日期</label>
                                    <input type="date" class="form-control" id="deadline" name="deadline" value="<%= project.getDeadline() != null ? project.getDeadline().toString() : "" %>">
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="categoryId" class="form-label">分类</label>
                                <select class="form-select" id="categoryId" name="categoryId">
                                    <option value="">请选择分类</option>
                                    <%
                                        if (categories != null) {
                                            for (Category c : categories) {
                                    %>
                                    <option value="<%= c.getId() %>" <%= project.getCategoryId() == c.getId() ? "selected" : "" %>><%= c.getName() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="status" class="form-label">状态</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="open" <%= "open".equals(project.getStatus()) ? "selected" : "" %>>开放中</option>
                                    <option value="closed" <%= "closed".equals(project.getStatus()) ? "selected" : "" %>>已关闭</option>
                                </select>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-gradient">保存修改</button>
                                <a href="${pageContext.request.contextPath}/project/detail?id=<%= project.getId() %>" class="btn btn-outline-secondary">取消</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
