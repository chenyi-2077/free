<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="chen_yi_an.User" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>编辑资料 - 自由人平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h3 class="card-title text-center mb-4">编辑资料</h3>
                        <%
                            String success = (String) request.getAttribute("success");
                            if (success != null) {
                        %>
                        <div class="alert alert-success"><%= success %></div>
                        <% } %>
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger"><%= error %></div>
                        <%
                            }
                            User user = (User) session.getAttribute("user");
                        %>
                        <form action="${pageContext.request.contextPath}/profile/edit" method="post">
                            <div class="mb-3">
                                <label for="displayName" class="form-label">显示名</label>
                                <input type="text" class="form-control" id="displayName" name="displayName"
                                       value="<%= user != null && user.getDisplayName() != null ? user.getDisplayName() : "" %>">
                            </div>
                            <div class="mb-3">
                                <label for="skills" class="form-label">技能</label>
                                <textarea class="form-control" id="skills" name="skills" rows="3"><%= user != null && user.getSkills() != null ? user.getSkills() : "" %></textarea>
                            </div>
                            <button type="submit" class="btn btn-gradient w-100">保存</button>
                        </form>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/profile">返回个人资料</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
