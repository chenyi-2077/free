<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>注册 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h3 class="card-title text-center mb-4">注册</h3>
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <form method="post">
                            <div class="mb-3">
                                <label class="form-label">邮箱</label>
                                <input type="email" name="email" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">密码</label>
                                <input type="password" name="password" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">身份</label>
                                <select name="role" class="form-select">
                                    <option value="employer">雇主</option>
                                    <option value="freelancer">自由职业者</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">显示名称</label>
                                <input type="text" name="displayName" class="form-control">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">技能标签</label>
                                <input type="text" name="skills" class="form-control" placeholder="如: Java, MySQL, Vue.js">
                            </div>
                            <button type="submit" class="btn btn-primary w-100">注册</button>
                        </form>
                        <p class="text-center mt-3">
                            已有账号？<a href="${pageContext.request.contextPath}/login">登录</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
