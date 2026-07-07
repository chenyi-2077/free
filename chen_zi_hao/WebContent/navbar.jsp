<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" import="chen_zi_hao.User" %>
<%
    User navUser = (User) session.getAttribute("user");
    String ctx = request.getContextPath();
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="<%= ctx %>/">Freelite</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="<%= ctx %>/orders">订单列表</a></li>
                <li class="nav-item"><a class="nav-link" href="<%= ctx %>/dashboard">数据看板</a></li>
            </ul>
            <ul class="navbar-nav">
                <% if (navUser != null) { %>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/profile"><%= navUser.getDisplayName() != null ? navUser.getDisplayName() : navUser.getEmail() %></a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/logout">退出登录</a></li>
                <% } else { %>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/login">登录</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/register">注册</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
