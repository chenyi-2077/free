<%@ page import="chen_xi_rui.User" pageEncoding="UTF-8" %>
<%
    User navUser = (User) session.getAttribute("user");
    String ctx = request.getContextPath();
    boolean loggedIn = navUser != null;
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="<%= ctx %>/">Freelite</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="<%= ctx %>/">首页</a></li>
                <% if (loggedIn) { %>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/my/bids">我的竞标</a></li>
                <% } %>
            </ul>
            <ul class="navbar-nav">
                <% if (loggedIn) { %>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/my/bids"><%= navUser.getDisplayName() != null ? navUser.getDisplayName() : navUser.getEmail() %></a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/">退出</a></li>
                <% } else { %>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/my/bids">我的竞标</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>