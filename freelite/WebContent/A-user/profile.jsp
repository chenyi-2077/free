<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freelite.model.User, java.util.List, com.freelite.model.Review" %>
<%
    User profileUser = (User) request.getAttribute("profileUser");
    List<Review> reviews = (List<Review>) request.getAttribute("reviews");
    Double avgRating = (Double) request.getAttribute("avgRating");
    if (profileUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>个人主页 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/WEB-INF/tags/navbar.jsp" %>
    <div class="container py-4">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h4><%= profileUser.getDisplayName() != null ? profileUser.getDisplayName() : "未命名" %></h4>
                <p class="text-muted">邮箱：<%= profileUser.getEmail() %></p>
                <p>身份：<%= "employer".equals(profileUser.getRole()) ? "雇主" : "自由职业者" %></p>
                <% if (profileUser.getSkills() != null && !profileUser.getSkills().isEmpty()) { %>
                    <p>技能：
                        <% for (String skill : profileUser.getSkills().split(",")) { %>
                            <span class="badge bg-secondary me-1"><%= skill.trim() %></span>
                        <% } %>
                    </p>
                <% } %>
                <p>评分：<span class="text-warning">★</span> <%= String.format("%.1f", avgRating != null ? avgRating : 0.0) %></p>
                <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-outline-primary btn-sm">编辑资料</a>
            </div>
        </div>
        <% if (reviews != null && !reviews.isEmpty()) { %>
            <div class="card shadow-sm">
                <div class="card-header"><h5 class="mb-0">收到的评价</h5></div>
                <div class="card-body">
                    <% for (Review r : reviews) { %>
                        <div class="border-bottom pb-2 mb-2">
                            <strong><%= r.getFromUserName() %></strong>
                            <span class="text-warning ms-2"><%= "★".repeat(r.getScore()) %></span>
                            <p class="mb-0 mt-1"><%= r.getComment() != null ? r.getComment() : "" %></p>
                            <small class="text-muted"><%= r.getCreatedAt() %></small>
                        </div>
                    <% } %>
                </div>
            </div>
        <% } %>
    </div>
</body>
</html>
