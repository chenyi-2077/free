<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freelite.model.User" %>
<%
    User loginUser = (User) session.getAttribute("user");
    User profileUser = (User) request.getAttribute("profileUser");
    Boolean isOwnProfile = (Boolean) request.getAttribute("isOwnProfile");
    if (profileUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= profileUser.getDisplayName() != null && !profileUser.getDisplayName().isEmpty() ? profileUser.getDisplayName() : "用户" %> - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .profile-header {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 3rem 0;
            border-radius: 0 0 20px 20px;
        }
        .avatar {
            width: 100px; height: 100px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            color: #667eea;
            margin: 0 auto;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }
        .skill-badge {
            background: rgba(102, 126, 234, 0.15);
            color: #667eea;
            border-radius: 20px;
            padding: 4px 12px;
            display: inline-block;
            margin: 2px;
            font-size: 0.85rem;
        }
        .star { color: #ffc107; }
    </style>
</head>
<body>

    <%-- 导航栏 --%>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: #667eea;">Freelite</a>
            <div class="d-flex align-items-center gap-3">
                <a href="<%= request.getContextPath() %>/projects" class="text-decoration-none text-muted">项目</a>
                <% if (isOwnProfile) { %>
                    <a href="<%= request.getContextPath() %>/dashboard" class="text-decoration-none text-muted">看板</a>
                    <a href="<%= request.getContextPath() %>/logout" class="text-decoration-none text-muted">退出</a>
                <% } %>
            </div>
        </div>
    </nav>

    <%-- 个人资料头部 --%>
    <div class="profile-header text-center">
        <div class="container">
            <div class="avatar">
                <%= profileUser.getDisplayName() != null && !profileUser.getDisplayName().isEmpty() ?
                    profileUser.getDisplayName().substring(0, 1).toUpperCase() : "U" %>
            </div>
            <h3 class="mt-3"><%= profileUser.getDisplayName() != null && !profileUser.getDisplayName().isEmpty() ? profileUser.getDisplayName() : "未设置昵称" %></h3>
            <p class="mb-1">
                <span class="badge bg-light text-dark"><%= "employer".equals(profileUser.getRole()) ? "雇主" : "自由职业者" %></span>
                <span class="ms-2">
                    <% int fullStars = (int) profileUser.getRating(); %>
                    <% for (int i = 0; i < 5; i++) { %>
                        <i class="bi bi-star<%= i < fullStars ? "-fill" : "" %> star"></i>
                    <% } %>
                    <small><%= profileUser.getRating() %></small>
                </span>
            </p>
            <p class="text-white-50 small"><%= profileUser.getEmail() %></p>
        </div>
    </div>

    <div class="container mt-4">
        <div class="row">
            <div class="col-md-8 mx-auto">

                <%-- 技能标签 --%>
                <div class="card mb-4">
                    <div class="card-body">
                        <h5><i class="bi bi-tools"></i> 技能</h5>
                        <% if (profileUser.getSkills() != null && !profileUser.getSkills().isEmpty()) { %>
                            <% for (String skill : profileUser.getSkills().split(",")) { %>
                                <span class="skill-badge"><%= skill.trim() %></span>
                            <% } %>
                        <% } else { %>
                            <p class="text-muted">暂未设置</p>
                        <% } %>
                    </div>
                </div>

                <%-- 如果是自己的主页，显示编辑表单 --%>
                <% if (isOwnProfile) { %>
                    <div class="card mb-4">
                        <div class="card-body">
                            <h5><i class="bi bi-pencil"></i> 编辑资料</h5>

                            <% String error = (String) request.getAttribute("error"); %>
                            <% if (error != null) { %>
                                <div class="alert alert-danger"><%= error %></div>
                            <% } %>
                            <% String success = (String) request.getAttribute("success"); %>
                            <% if (success != null) { %>
                                <div class="alert alert-success"><%= success %></div>
                            <% } %>

                            <form action="<%= request.getContextPath() %>/profile" method="post">
                                <div class="mb-3">
                                    <label class="form-label">昵称</label>
                                    <input type="text" name="displayName" class="form-control"
                                           value="<%= profileUser.getDisplayName() != null ? profileUser.getDisplayName() : "" %>">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">技能</label>
                                    <input type="text" name="skills" class="form-control"
                                           value="<%= profileUser.getSkills() != null ? profileUser.getSkills() : "" %>"
                                           placeholder="用逗号分隔：Java, Spring, MySQL">
                                </div>
                                <button type="submit" class="btn btn-primary">保存修改</button>
                            </form>
                        </div>
                    </div>
                <% } %>

            </div>
        </div>
    </div>

</body>
</html>
