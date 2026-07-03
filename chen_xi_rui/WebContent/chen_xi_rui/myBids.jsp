<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的竞标 - FreeLite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
                <i class="bi bi-lightning-charge-fill"></i> FreeLite
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                            <i class="bi bi-house"></i> 首页
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/my/bids">
                            <i class="bi bi-person"></i> 我的竞标
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">

        <!-- 页面标题 -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h4 class="mb-1"><i class="bi bi-person-badge text-primary"></i> 我的竞标记录</h4>
                <p class="text-muted mb-0 small">共 ${bids.size()} 条竞标</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-primary">
                    <i class="bi bi-arrow-left"></i> 返回首页
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty bids}">
                <div class="card shadow-sm border-0">
                    <div class="card-body text-center py-5">
                        <i class="bi bi-inbox" style="font-size: 3rem; color: #dee2e6;"></i>
                        <h5 class="text-muted mt-3">暂无竞标记录</h5>
                        <p class="text-muted small">您还没有提交过任何竞标，快去项目中报价吧！</p>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                            <i class="bi bi-search"></i> 浏览项目
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-3">
                    <c:forEach var="bid" items="${bids}">
                        <div class="col-12">
                            <div class="card shadow-sm border-0">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                        <div>
                                            <h6 class="mb-1">
                                                <i class="bi bi-folder text-primary"></i>
                                                项目 #${bid.projectId}
                                            </h6>
                                            <small class="text-muted">
                                                <i class="bi bi-clock-history"></i>
                                                提交于 ${bid.createdAt}
                                            </small>
                                        </div>
                                        <div class="text-end">
                                            <h5 class="text-primary mb-0">¥${bid.amount}</h5>
                                            <small class="text-muted">${bid.days} 天</small>
                                        </div>
                                    </div>

                                    <div class="p-3 bg-light rounded-3 mb-2">
                                        <p class="mb-0 text-secondary">${bid.proposal}</p>
                                    </div>

                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <c:choose>
                                                <c:when test="${bid.status eq 'pending'}">
                                                    <span class="badge bg-warning bg-opacity-25 text-warning px-3 py-2">
                                                        <i class="bi bi-clock"></i> 待审核
                                                    </span>
                                                </c:when>
                                                <c:when test="${bid.status eq 'accepted'}">
                                                    <span class="badge bg-success bg-opacity-25 text-success px-3 py-2">
                                                        <i class="bi bi-check-circle"></i> 已中标
                                                    </span>
                                                </c:when>
                                                <c:when test="${bid.status eq 'rejected'}">
                                                    <span class="badge bg-danger bg-opacity-25 text-danger px-3 py-2">
                                                        <i class="bi bi-x-circle"></i> 未中标
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary bg-opacity-25 text-secondary px-3 py-2">
                                                        ${bid.status}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/bids?projectId=${bid.projectId}"
                                           class="btn btn-outline-primary btn-sm">
                                            <i class="bi bi-eye"></i> 查看项目竞标
                                        </a>
                                    </div>
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
