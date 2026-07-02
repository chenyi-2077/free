<%@ tag body-content="scriptless" %>
<%@ attribute name="title" required="false" %>
<nav class="navbar navbar-expand-lg" style="background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05);">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/projects" style="color: #667eea;">Freelite</a>
        <div class="d-flex align-items-center gap-3">
            <a href="${pageContext.request.contextPath}/projects" class="text-decoration-none text-muted">项目</a>
            <a href="${pageContext.request.contextPath}/my/projects" class="text-decoration-none text-muted">我的项目</a>
            <a href="${pageContext.request.contextPath}/orders" class="text-decoration-none text-muted">订单</a>
            <a href="${pageContext.request.contextPath}/wallet" class="text-decoration-none text-muted">钱包</a>
            <a href="${pageContext.request.contextPath}/dashboard" class="text-decoration-none text-muted">看板</a>
            <a href="${pageContext.request.contextPath}/profile" class="text-decoration-none text-muted">个人主页</a>
            <a href="${pageContext.request.contextPath}/logout" class="text-decoration-none text-muted">退出</a>
        </div>
    </div>
</nav>
