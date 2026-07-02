<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg" style="background: var(--okx-bg-secondary); border-bottom: 1px solid var(--okx-border);">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/projects" style="color: var(--okx-accent);">Freelite</a>
        <div class="d-flex align-items-center gap-3">
            <a href="${pageContext.request.contextPath}/projects" class="text-decoration-none" style="color: var(--okx-text-secondary);">项目</a>
            <a href="${pageContext.request.contextPath}/my/projects" class="text-decoration-none" style="color: var(--okx-text-secondary);">我的项目</a>
            <a href="${pageContext.request.contextPath}/orders" class="text-decoration-none" style="color: var(--okx-text-secondary);">订单</a>
            <a href="${pageContext.request.contextPath}/wallet" class="text-decoration-none" style="color: var(--okx-text-secondary);">钱包</a>
            <a href="${pageContext.request.contextPath}/dashboard" class="text-decoration-none" style="color: var(--okx-text-secondary);">看板</a>
            <a href="${pageContext.request.contextPath}/profile" class="text-decoration-none" style="color: var(--okx-text-secondary);">个人主页</a>
            <a href="${pageContext.request.contextPath}/logout" class="text-decoration-none" style="color: var(--okx-text-secondary);">退出</a>
        </div>
    </div>
</nav>
