<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth-light.css">
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="card p-4 text-center">
                    <div style="font-size: 2.5rem;" class="logo-gradient mb-2">Freelite</div>
                    <p class="text-muted">轻量化的自由职业项目竞标平台</p>

                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger mt-3"><%= error %></div>
                    <% } %>

                    <% String success = (String) request.getAttribute("success"); %>
                    <% if (success != null) { %>
                        <div class="alert alert-success mt-3"><%= success %></div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/login" method="post" class="mt-3 text-start">
                        <div class="mb-3">
                            <label class="form-label fw-bold">邮箱</label>
                            <input type="email" name="email" class="form-control" placeholder="your@email.com" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">密码</label>
                            <input type="password" name="password" class="form-control" placeholder="输入密码" required>
                        </div>
                        <button type="submit" class="btn btn-gradient w-100">登 录</button>
                    </form>

                    <p class="text-muted mt-3">
                        还没有账号？<a href="${pageContext.request.contextPath}/register" style="color: #00C897; font-weight: 600;">立即注册 →</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
