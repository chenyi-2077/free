<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单详情 - Freelite</title>
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
            <li class="breadcrumb-item active">订单详情</li>
        </ol>
    </nav>

    <div class="card shadow-sm border-0 mb-4">
        <div class="card-header bg-white py-3">
            <h4 class="card-title mb-0">${order.projectTitle}</h4>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <table class="table table-borderless">
                        <tr>
                            <th style="width:120px;">订单编号</th>
                            <td>#${order.id}</td>
                        </tr>
                        <tr>
                            <th>项目名称</th>
                            <td>${order.projectTitle}</td>
                        </tr>
                        <tr>
                            <th>雇主</th>
                            <td>${order.employerName}</td>
                        </tr>
                        <tr>
                            <th>自由职业者</th>
                            <td>${order.freelancerName}</td>
                        </tr>
                        <tr>
                            <th>金额</th>
                            <td><strong>¥${order.amount}</strong></td>
                        </tr>
                        <tr>
                            <th>状态</th>
                            <td>
                                <span class="badge
                                    <c:choose>
                                        <c:when test="${order.status == 'in_progress'}">bg-warning text-dark</c:when>
                                        <c:when test="${order.status == 'awaiting_confirm'}">bg-info text-dark</c:when>
                                        <c:when test="${order.status == 'completed'}">bg-success</c:when>
                                        <c:otherwise>bg-secondary</c:otherwise>
                                    </c:choose>
                                ">${order.status}</span>
                            </td>
                        </tr>
                        <tr>
                            <th>创建时间</th>
                            <td>${order.createdAt}</td>
                        </tr>
                    </table>
                </div>
            </div>

            <!-- 操作按钮 -->
            <div class="mt-3">
                <c:if test="${order.status == 'in_progress'}">
                    <form action="${pageContext.request.contextPath}/order/complete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${order.id}">
                        <button type="submit" class="btn btn-success">标记完成（待确认）</button>
                    </form>
                </c:if>
                <c:if test="${order.status == 'awaiting_confirm'}">
                    <form action="${pageContext.request.contextPath}/order/confirm" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${order.id}">
                        <button type="submit" class="btn btn-primary">确认完成</button>
                    </form>
                </c:if>
                <c:if test="${order.status == 'completed' && empty review}">
                    <a href="${pageContext.request.contextPath}/review?orderId=${order.id}" class="btn btn-outline-warning">发表评价</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary">返回列表</a>
            </div>
        </div>
    </div>

    <!-- 评价信息 -->
    <c:if test="${not empty review}">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3">
                <h5 class="card-title mb-0">订单评价</h5>
            </div>
            <div class="card-body">
                <p><strong>评价人：</strong>${review.fromUserName}</p>
                <p><strong>评分：</strong>
                    <c:forEach begin="1" end="5" var="i">
                        <span class="text-warning" style="font-size:1.2rem;">
                            <c:choose>
                                <c:when test="${i <= review.score}">&#9733;</c:when>
                                <c:otherwise>&#9734;</c:otherwise>
                            </c:choose>
                        </span>
                    </c:forEach>
                    <span class="ms-2">${review.score}/5</span>
                </p>
                <c:if test="${not empty review.comment}">
                    <p><strong>评语：</strong></p>
                    <p class="text-muted">${review.comment}</p>
                </c:if>
                <p class="text-muted small">${review.createdAt}</p>
            </div>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
