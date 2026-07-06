<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="chen_xi_rui.User" %>
<%
    User bfu = (User) session.getAttribute("user");
    String bfProjectId = String.valueOf(request.getAttribute("projectId"));

%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>提交竞标 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h4>提交竞标</h4>
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <form method="post" action="<%= ctx %>/bid/place">
                            <input type="hidden" name="projectId" value="<%= bfProjectId %>">
                            <div class="mb-3">
                                <label class="form-label">报价 (¥)</label>
                                <input type="number" name="amount" class="form-control" step="0.01" min="0" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">工期 (天)</label>
                                <input type="number" name="days" class="form-control" min="1" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">方案描述</label>
                                <textarea name="proposal" class="form-control" rows="5" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-gradient">提交竞标</button>
                            <a href="<%= ctx %>/bids?projectId=<%= bfProjectId %>" class="btn btn-outline-secondary">返回</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
