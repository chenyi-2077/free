<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - Freelite</title>
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
        .logo span { color: #764ba2; }
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
        .form-control, .form-select {
            border-radius: 8px;
            padding: 12px;
        }
        .role-card {
            cursor: pointer;
            border: 2px solid #e0e0e0;
            border-radius: 12px;
            padding: 1rem;
            text-align: center;
            transition: all 0.2s;
        }
        .role-card:hover {
            border-color: #667eea;
        }
        .role-card.selected {
            border-color: #667eea;
            background: #f0f0ff;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">

                <div class="card">
                    <div class="card-header">
                        <div class="logo">Freelite<span>.</span></div>
                        <p class="text-muted mt-2">创建你的账号</p>
                    </div>
                    <div class="card-body p-4">

                        <% String error = (String) request.getAttribute("error"); %>
                        <% if (error != null) { %>
                            <div class="alert alert-danger"><%= error %></div>
                        <% } %>

                        <form action="<%= request.getContextPath() %>/register" method="post">

                            <%-- 角色选择 --%>
                            <div class="mb-4">
                                <label class="form-label fw-bold">我是</label>
                                <div class="row g-2">
                                    <div class="col-6">
                                        <div class="role-card" onclick="selectRole('employer')">
                                            <div style="font-size: 2rem;">📋</div>
                                            <div class="fw-bold">雇主</div>
                                            <small class="text-muted">发布项目，找人做</small>
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
                                <label class="form-label">邮箱 <span class="text-danger">*</span></label>
                                <input type="email" name="email" class="form-control" placeholder="your@email.com" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">密码 <span class="text-danger">*</span></label>
                                <input type="password" name="password" class="form-control" placeholder="至少6位" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">昵称/公司名</label>
                                <input type="text" name="displayName" class="form-control" placeholder="别人怎么称呼你">
                            </div>
                            <div class="mb-4">
                                <label class="form-label">技能标签</label>
                                <input type="text" name="skills" class="form-control" placeholder="如：Java, Spring, MySQL">
                                <small class="text-muted">用逗号分隔</small>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">创建账号</button>
                        </form>

                        <div class="text-center mt-3">
                            <span class="text-muted">已有账号？</span>
                            <a href="<%= request.getContextPath() %>/login" class="text-decoration-none fw-bold" style="color: #764ba2;">立即登录 →</a>
                        </div>
                    </div>
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
        // 默认选中自由职业者
        document.querySelector('.role-card:last-child').classList.add('selected');
    </script>
</body>
</html>
