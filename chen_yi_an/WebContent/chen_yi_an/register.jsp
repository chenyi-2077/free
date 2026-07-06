<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>注册 - 自由人平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth-light.css">
</head>
<body>
    <%@ include file="/navbar.jsp" %>
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card p-4 text-center">
                    <div class="card-body p-4">
                        <div style="font-size: 2rem;" class="logo-gradient mb-2 text-center">Freelite</div>
                    <p class="text-muted text-center">创建你的账号</p>
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger"><%= error %></div>
                        <% } %>
                        <form action="${pageContext.request.contextPath}/register" method="post">
                            <div class="mb-3">
                                <label for="email" class="form-label">邮箱</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">密码</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="role" class="form-label">角色</label>
                                <select class="form-select" id="role" name="role">
                                    <option value="freelancer">自由人</option>
                                    <option value="employer">雇主</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="displayName" class="form-label">显示名</label>
                                <input type="text" class="form-control" id="displayName" name="displayName">
                            </div>
                            <div class="mb-3">
                                <label for="skills" class="form-label">技能</label>
                                <textarea class="form-control" id="skills" name="skills" rows="3" placeholder="请描述您的技能，多个技能用逗号分隔"></textarea>
                            </div>
                            <button type="submit" class="btn btn-gradient w-100">注册</button>
                        </form>
                        <div class="text-center mt-3">
                            <a style="color: #00C897; font-weight: 600;" href="${pageContext.request.contextPath}/login">已有账号？立即登录</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
