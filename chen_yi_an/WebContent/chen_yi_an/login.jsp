<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>登录 - 自由人平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth-light.css">
</head>
<body>
    <%@ include file="/navbar.jsp" %>
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-5">
                <div class="card p-4 text-center">
                    <div class="card-body p-4">
                        <div style="font-size: 2.5rem;" class="logo-gradient mb-2 text-center">Freelite</div>
                    <p class="text-muted text-center">轻量化的自由职业项目竞标平台</p>
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger"><%= error %></div>
                        <% } %>
                        <form action="${pageContext.request.contextPath}/login" method="post">
                            <div class="mb-3">
                                <label for="email" class="form-label">邮箱</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">密码</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <button type="submit" class="btn btn-gradient w-100">登录</button>
                        </form>
                        <div class="text-center mt-3">
                            <a style="color: #00C897; font-weight: 600;" href="${pageContext.request.contextPath}/register">还没有账号？立即注册</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
