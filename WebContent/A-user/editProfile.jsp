<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freelite.model.User" %>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑资料 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .btn-primary {
            background: var(--accent);
            border: none; border-radius: 8px; padding: 12px; font-weight: 600;
        }
        .form-control { border-radius: 8px; padding: 12px; }
        .btn-outline-secondary { border-radius: 8px; padding: 12px; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: var(--accent);">Freelite</a>
            <a href="<%= request.getContextPath() %>/profile" class="text-decoration-none text-muted">← 返回</a>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card p-4">
                    <h4 class="fw-bold mb-4">✏️ 编辑资料</h4>

                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger"><%= error %></div>
                    <% } %>
                    <% String success = (String) request.getAttribute("success"); %>
                    <% if (success != null) { %>
                        <div class="alert alert-success"><%= success %></div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/profile/edit" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-bold">邮箱</label>
                            <input type="email" class="form-control" value="<%= loginUser.getEmail() %>" disabled>
                            <small class="text-muted">邮箱不可修改</small>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">昵称 / 公司名</label>
                            <input type="text" name="displayName" class="form-control"
                                   value="<%= loginUser.getDisplayName() != null ? loginUser.getDisplayName() : "" %>"
                                   placeholder="别人怎么称呼你">
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-bold">技能标签</label>
                            <input type="text" name="skills" class="form-control"
                                   value="<%= loginUser.getSkills() != null ? loginUser.getSkills() : "" %>"
                                   placeholder="Java, Spring, MySQL, Vue.js">
                            <small class="text-muted">用英文逗号分隔</small>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary flex-grow-1">保存修改</button>
                            <a href="<%= request.getContextPath() %>/profile" class="btn btn-outline-secondary flex-grow-1">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
