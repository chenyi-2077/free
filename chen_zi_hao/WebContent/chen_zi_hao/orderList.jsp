<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单列表 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/orders">Freelite</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/orders">订单列表</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">数据看板</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container py-4">
    <h2 class="mb-4">订单列表</h2>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="alert alert-info">暂无订单数据。</div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <c:forEach var="order" items="${orders}">
                    <div class="col">
                        <div class="card h-100 shadow-sm border-0">
                            <div class="card-body">
                                <h5 class="card-title text-truncate">${order.projectTitle}</h5>
                                <p class="card-text mb-1">
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${order.status == 'in_progress'}">bg-warning text-dark</c:when>
                                            <c:when test="${order.status == 'awaiting_confirm'}">bg-info text-dark</c:when>
                                            <c:when test="${order.status == 'completed'}">bg-success</c:when>
                                            <c:otherwise>bg-secondary</c:otherwise>
                                        </c:choose>
                                    ">${order.status}</span>
                                </p>
                                <p class="card-text mb-1"><strong>金额：</strong>¥${order.amount}</p>
                                <p class="card-text mb-1"><strong>雇主：</strong>${order.employerName}</p>
                                <p class="card-text mb-1"><strong>自由职业者：</strong>${order.freelancerName}</p>
                                <p class="card-text text-muted small">${order.createdAt}</p>
                            </div>
                            <div class="card-footer bg-white border-0">
                                <a href="${pageContext.request.contextPath}/order/detail?id=${order.id}" class="btn btn-outline-primary btn-sm w-100">查看详情</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
