<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        body {
            background: var(--okx-bg-primary);
            min-height: 100vh;
            display: flex;
            align-items: center;
        }
        .role-card {
            cursor: pointer;
            border: 2px solid var(--okx-border);
            border-radius: 12px;
            padding: 1rem;
            text-align: center;
            transition: all 0.2s;
        }
        .role-card:hover { border-color: var(--okx-accent); }
        .role-card.selected {
            border-color: var(--okx-accent);
            background: var(--okx-accent-dim);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card p-4">
                    <div class="text-center mb-4">
                        <div style="font-size: 2rem;" class="logo-gradient">Freelite</div>
                        <p class="text-muted">创建你的账号</p>
                    </div>

                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger"><%= error %></div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-bold">我是</label>
                            <div class="row g-2">
                                <div class="col-6">
                                    <div class="role-card" onclick="selectRole('employer')">
                                        <div style="font-size: 2rem;">📋</div>
                                        <div class="fw-bold">雇主</div>
                                        <small class="text-muted">发布项目</small>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="role-card" onclick="selectRole('freelancer')">
                                        <div style="font-size: 2rem;">💻</div>
                                        <div class="fw-bold">自由职业者</div>
                                        <small class="text-muted">接单赚钱</small>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="role" id="roleInput" value="freelancer">
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">邮箱 <span class="text-danger">*</span></label>
                            <input type="email" name="email" class="form-control" placeholder="your@email.com" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">密码 <span class="text-danger">*</span></label>
                            <input type="password" name="password" class="form-control" placeholder="至少6位" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">昵称</label>
                            <input type="text" name="displayName" class="form-control" placeholder="别人怎么称呼你">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">技能标签</label>
                            <input type="text" name="skills" class="form-control" placeholder="Java, Spring, MySQL">
                            <small class="text-muted">用逗号分隔</small>
                        </div>

                        <button type="submit" class="btn btn-gradient w-100">创建账号</button>
                    </form>

                    <p class="text-muted text-center mt-3">
                        已有账号？<a href="${pageContext.request.contextPath}/login" class="fw-bold">立即登录 →</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <script>
        function selectRole(role) {
            document.getElementById('roleInput').value = role;
            document.querySelectorAll('.role-card').forEach(c => c.classList.remove('selected'));
            event.currentTarget.classList.add('selected');
        }
        document.querySelector('.role-card:last-child').classList.add('selected');
    </script>
</body>
</html>
