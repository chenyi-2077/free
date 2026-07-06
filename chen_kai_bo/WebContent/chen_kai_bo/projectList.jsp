<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="chen_kai_bo.Project" %>
<%@ page import="chen_kai_bo.Category" %>
<%
    List<Project> projects = (List<Project>) request.getAttribute("projects");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String selectedKeyword = (String) request.getAttribute("selectedKeyword");
    Integer selectedCategoryId = (Integer) request.getAttribute("selectedCategoryId");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>项目列表 - FreeLite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>

    <div class="container py-4">
        <h3 class="mb-4">项目列表</h3>
        <!-- 搜索和筛选 -->
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/projects" method="get" class="row g-3">
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="keyword" placeholder="搜索项目标题或描述..." value="<%= selectedKeyword != null ? selectedKeyword : "" %>">
                    </div>
                    <div class="col-md-4">
                        <select class="form-select" name="categoryId">
                            <option value="">全部分类</option>
                            <%
                                if (categories != null) {
                                    for (Category c : categories) {
                            %>
                            <option value="<%= c.getId() %>" <%= (selectedCategoryId != null && selectedCategoryId == c.getId()) ? "selected" : "" %>><%= c.getName() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-gradient w-100">搜索</button>
                    </div>
                </form>
            </div>
        </div>
        <!-- 项目卡片列表 -->
        <%
            if (projects != null && !projects.isEmpty()) {
                for (Project p : projects) {
        %>
        <div class="card shadow-sm mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h5 class="card-title mb-1">
                            <a href="${pageContext.request.contextPath}/project/detail?id=<%= p.getId() %>" class="text-decoration-none"><%= p.getTitle() %></a>
                        </h5>
                        <p class="text-muted mb-2"><%= p.getDescription() != null && p.getDescription().length() > 150 ? p.getDescription().substring(0, 150) + "..." : p.getDescription() %></p>
                    </div>
                    <span class="badge <%= "open".equals(p.getStatus()) ? "bg-success" : "bg-secondary" %>"><%= p.getStatus() %></span>
                </div>
                <div class="d-flex flex-wrap gap-3 mt-2 text-muted small">
                    <span>💰 ¥<%= String.format("%.2f", p.getBudget()) %></span>
                    <span>📂 <%= p.getCategoryName() != null ? p.getCategoryName() : "未分类" %></span>
                    <span>👤 <%= p.getEmployerName() != null ? p.getEmployerName() : "未知" %></span>
                    <span>📅 截止: <%= p.getDeadline() != null ? p.getDeadline().toString() : "未设置" %></span>
                </div>
            </div>
        </div>
        <%
                }
            } else {
        %>
        <div class="alert alert-info text-center">暂无项目</div>
        <%
            }
        %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
