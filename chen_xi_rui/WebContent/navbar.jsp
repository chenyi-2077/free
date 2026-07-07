<%@ page pageEncoding="UTF-8" %>
<%
    String ctx = request.getContextPath();
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="<%= ctx %>/">Freelite 竞标系统</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="<%= ctx %>/">首页</a></li>
            </ul>
        </div>
    </div>
</nav>