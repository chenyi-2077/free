<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="chen_yi_an.User" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>个人资料 - 自由人平台</title>
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
                        <h3 class="card-title text-center mb-4">个人资料</h3>
                        <%
                            User profileUser = (User) request.getAttribute("profileUser");
                            if (profileUser != null) {
                        %>
                        <div class="mb-3">
                            <label class="form-label text-muted">邮箱</label>
                            <p class="form-control-plaintext"><%= profileUser.getEmail() != null ? profileUser.getEmail() : "" %></p>
                        </div>
                            <label class="form-label text-muted">显示名</label>
                            <p class="form-control-plaintext"><%= profileUser.getDisplayName() != null ? profileUser.getDisplayName() : "" %></p>
                            <label class="form-label text-muted">角色</label>
                            <p class="form-control-plaintext"><%= profileUser.getRole() != null ? profileUser.getRole() : "" %></p>
                            <label class="form-label text-muted">技能</label>
                            <p class="form-control-plaintext">
                            <%
                                String skills = profileUser.getSkills();
                                if (skills != null && !skills.isEmpty()) {
                                    String[] skillArr = skills.split(",");
                                    for (String skill : skillArr) {
                            %>
                                <span class="badge bg-primary me-1"><%= skill.trim() %></span>
                                    }
                                }
                            </p>
                            <label class="form-label text-muted">评分</label>
                            <p class="form-control-plaintext"><%= String.format("%.1f", profileUser.getRating()) %> / 5.0</p>
                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-gradient">编辑资料</a>
                        <% } else { %>
                        <p class="text-muted text-center">请先登录</p>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-gradient">去登录</a>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
