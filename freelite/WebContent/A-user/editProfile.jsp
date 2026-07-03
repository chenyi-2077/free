<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freelite.model.User" %>
<%
    User editUser = (User) session.getAttribute("user");
    if (editUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>编辑资料 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h4>编辑资料</h4>
                        <% if (request.getAttribute("success") != null) { %>
                            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
                        <% } %>
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <form method="post">
                            <div class="mb-3">
                                <label class="form-label">显示名称</label>
                                <input type="text" name="displayName" class="form-control" value="<%= editUser.getDisplayName() != null ? editUser.getDisplayName() : "" %>">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">技能标签</label>
                                <input type="text" name="skills" class="form-control" value="<%= editUser.getSkills() != null ? editUser.getSkills() : "" %>" placeholder="如: Java, MySQL, Vue.js">
                            </div>
                            <button type="submit" class="btn btn-primary">保存</button>
                            <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-secondary">取消</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
