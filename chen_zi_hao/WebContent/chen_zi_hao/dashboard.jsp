<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>数据看板 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/orders">Freelite</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/orders">订单列表</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">数据看板</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container py-4">
    <h2 class="mb-4">数据看板</h2>

    <div class="row g-4">
        <!-- 项目数 -->
        <div class="col-md-3">
            <div class="card shadow-sm border-0 text-center h-100">
                <div class="card-body">
                    <div class="display-4 text-primary fw-bold">${stats.projectCount}</div>
                    <p class="text-muted mt-2 mb-0">项目总数</p>
                </div>
            </div>
        </div>

        <!-- 竞标数 -->
        <div class="col-md-3">
            <div class="card shadow-sm border-0 text-center h-100">
                <div class="card-body">
                    <div class="display-4 text-info fw-bold">${stats.bidCount}</div>
                    <p class="text-muted mt-2 mb-0">竞标总数</p>
                </div>
            </div>
        </div>

        <!-- 订单数 -->
        <div class="col-md-3">
            <div class="card shadow-sm border-0 text-center h-100">
                <div class="card-body">
                    <div class="display-4 text-success fw-bold">${stats.orderCount}</div>
                    <p class="text-muted mt-2 mb-0">订单总数</p>
                </div>
            </div>
        </div>

        <!-- 评价数 -->
        <div class="col-md-3">
            <div class="card shadow-sm border-0 text-center h-100">
                <div class="card-body">
                    <div class="display-4 text-warning fw-bold">${stats.reviewCount}</div>
                    <p class="text-muted mt-2 mb-0">评价总数</p>
                </div>
            </div>
        </div>
    </div>

    <div class="mt-5">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3">
                <h5 class="card-title mb-0">平台概况</h5>
            </div>
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <th style="width:200px;">项目总数</th>
                        <td>${stats.projectCount}</td>
                    </tr>
                    <tr>
                        <th>竞标总数</th>
                        <td>${stats.bidCount}</td>
                    </tr>
                    <tr>
                        <th>订单总数</th>
                        <td>${stats.orderCount}</td>
                    </tr>
                    <tr>
                        <th>评价总数</th>
                        <td>${stats.reviewCount}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
