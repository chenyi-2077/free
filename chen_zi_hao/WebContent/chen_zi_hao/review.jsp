<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>发表评价 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/orders">Freelite</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/orders">订单列表</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">数据看板</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container py-4">

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders">订单列表</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/order/detail?id=${order.id}">订单详情</a></li>
            <li class="breadcrumb-item active">发表评价</li>
        </ol>
    </nav>

    <div class="card shadow-sm border-0">
        <div class="card-header bg-white py-3">
            <h4 class="card-title mb-0">对订单「${order.projectTitle}」发表评价</h4>
        </div>
        <div class="card-body">
            <p class="text-muted mb-4">雇主：${order.employerName} | 自由职业者：${order.freelancerName} | 金额：¥${order.amount}</p>

            <form action="${pageContext.request.contextPath}/review" method="post">
                <input type="hidden" name="orderId" value="${order.id}">

                <div class="mb-4">
                    <label class="form-label"><strong>评分</strong></label>
                    <div class="d-flex gap-3">
                        <c:forEach begin="1" end="5" var="i">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="score" id="score${i}" value="${i}" required>
                                <label class="form-check-label" for="score${i}">
                                    <c:forEach begin="1" end="${i}">&#9733;</c:forEach>
                                    <c:if test="${i < 5}"><c:forEach begin="1" end="${5-i}">&#9734;</c:forEach></c:if>
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="mb-4">
                    <label for="comment" class="form-label"><strong>评论（选填）</strong></label>
                    <textarea class="form-control" id="comment" name="comment" rows="4" placeholder="请描述您的体验..."></textarea>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">提交评价</button>
                    <a href="${pageContext.request.contextPath}/order/detail?id=${order.id}" class="btn btn-outline-secondary">取消</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
