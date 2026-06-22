<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
        }
        .card {
            border: none;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
        }
        .card-header {
            background: transparent;
            border-bottom: none;
            text-align: center;
            padding-top: 2rem;
        }
        .logo {
            font-size: 2rem;
            font-weight: 700;
            color: #667eea;
        }
        .logo span {
            color: #764ba2;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            border-radius: 8px;
            padding: 12px;
            font-weight: 600;
        }
        .btn-primary:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }
        .form-control {
            border-radius: 8px;
            padding: 12px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">

                <div class="card">
                    <div class="card-header">
                        <div class="logo">Freelite<span>.</span></div>
                        <p class="text-muted mt-2">欢迎回来</p>
                    </div>
                    <div class="card-body p-4">

                        <%-- 错误提示 --%>
                        <% String error = (String) request.getAttribute("error"); %>
                        <% if (error != null) { %>
                            <div class="alert alert-danger"><%= error %></div>
                        <% } %>

                        <%-- 成功提示 --%>
                        <% String success = (String) request.getAttribute("success"); %>
                        <% if (success != null) { %>
                            <div class="alert alert-success"><%= success %></div>
                        <% } %>

                        <form action="<%= request.getContextPath() %>/login" method="post">
                            <div class="mb-3">
                                <label class="form-label">邮箱</label>
                                <input type="email" name="email" class="form-control" placeholder="请输入邮箱" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">密码</label>
                                <input type="password" name="password" class="form-control" placeholder="请输入密码" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">登 录</button>
                        </form>

                        <div class="text-center mt-3">
                            <span class="text-muted">还没有账号？</span>
                            <a href="<%= request.getContextPath() %>/register" class="text-decoration-none fw-bold" style="color: #764ba2;">立即注册 →</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</body>
</html>
