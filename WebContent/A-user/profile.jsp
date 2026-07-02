<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freelite.model.User" %>
<%
    User loginUser = (User) session.getAttribute("user");
    User profileUser = (User) request.getAttribute("profileUser");
    Boolean isOwnProfile = (Boolean) request.getAttribute("isOwnProfile");
    if (profileUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    if (isOwnProfile == null) isOwnProfile = false;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= profileUser.getDisplayName() != null && !profileUser.getDisplayName().isEmpty() ? profileUser.getDisplayName() : "用户" %> - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        .profile-header {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 3rem 0;
            border-radius: 0 0 24px 24px;
        }
        .avatar {
            width: 96px; height: 96px;
            background: white; border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 2.5rem; color: #667eea;
            margin: 0 auto; box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/projects" class="nav-link">项目</a>
                <a href="${pageContext.request.contextPath}/my/projects" class="nav-link">我的项目</a>
                <a href="${pageContext.request.contextPath}/orders" class="nav-link">订单</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">看板</a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">退出</a>
            </div>
        </div>
    </nav>

    <div class="profile-header text-center">
        <div class="container">
            <div class="avatar">
                <%= profileUser.getDisplayName() != null && !profileUser.getDisplayName().isEmpty() ?
                    profileUser.getDisplayName().substring(0, 1).toUpperCase() : "U" %>
            </div>
            <h3 class="mt-3"><%= profileUser.getDisplayName() != null && !profileUser.getDisplayName().isEmpty() ? profileUser.getDisplayName() : "未设置昵称" %></h3>
            <p>
                <span class="badge bg-light text-dark"><%= "employer".equals(profileUser.getRole()) ? "雇主" : "自由职业者" %></span>
                <span class="ms-2">
                    <% int fullStars = (int) profileUser.getRating(); %>
                    <% for (int i = 0; i < 5; i++) { %>
                        <i class="bi bi-star<%= i < fullStars ? "-fill" : "" %> star"></i>
                    <% } %>
                    <small><%= profileUser.getRating() %></small>
                </span>
            </p>
        </div>
    </div>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">

                <%-- 技能 --%>
                <div class="card p-4 mb-3">
                    <h5 class="fw-bold mb-3"><i class="bi bi-tools"></i> 技能</h5>
                    <% if (profileUser.getSkills() != null && !profileUser.getSkills().isEmpty()) { %>
                        <% for (String skill : profileUser.getSkills().split(",")) { %>
                            <span class="skill-badge"><%= skill.trim() %></span>
                        <% } %>
                    <% } else { %>
                        <p class="text-muted">暂未设置</p>
                    <% } %>
                </div>

                <%-- 编辑按钮 --%>
                <% if (isOwnProfile) { %>
                    <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-gradient w-100 mb-3">
                        <i class="bi bi-pencil"></i> 编辑资料
                    </a>
                <% } %>

                <%-- 查看其他人的竞标 --%>
                <% if (isOwnProfile && "freelancer".equals(profileUser.getRole())) { %>
                    <a href="${pageContext.request.contextPath}/my/bids" class="btn btn-outline-primary w-100">
                        <i class="bi bi-send"></i> 我的竞标记录
                    </a>
                <% } %>

            </div>
        </div>
    </div>
    <style>
        .btn-outline-primary {
            border: 1.5px solid #667eea; color: #667eea;
            border-radius: 8px; padding: 10px; font-weight: 600;
        }
        .btn-outline-primary:hover { background: #667eea; color: white; }
    </style>
</body>
</html>
