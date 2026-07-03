<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="chen_xi_rui.Bid" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>竞标列表 - FreeLite</title>
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/bid/place?projectId=${projectId}">
                            <i class="bi bi-send-plus"></i> 提交竞标
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/my/bids">
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
                <h4 class="mb-1"><i class="bi bi-list-check text-primary"></i> 项目竞标列表</h4>
                <p class="text-muted mb-0 small">项目 #${projectId} · 共 ${bids.size()} 个竞标</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/bid/place?projectId=${projectId}"
                   class="btn btn-primary">
                    <i class="bi bi-send-plus"></i> 提交竞标
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty bids}">
                <div class="card shadow-sm border-0">
                    <div class="card-body text-center py-5">
                        <i class="bi bi-inbox" style="font-size: 3rem; color: #dee2e6;"></i>
                        <h5 class="text-muted mt-3">暂无竞标</h5>
                        <p class="text-muted small">还没有人提交竞标，快来第一个报价吧！</p>
                        <a href="${pageContext.request.contextPath}/bid/place?projectId=${projectId}"
                           class="btn btn-primary">
                            <i class="bi bi-send-plus"></i> 提交竞标
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
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center"
                                                 style="width: 48px; height: 48px;">
                                                <i class="bi bi-person-circle text-primary" style="font-size: 1.5rem;"></i>
                                            </div>
                                            <div>
                                                <h6 class="mb-1">${bid.freelancerName}</h6>
                                                <div class="d-flex align-items-center gap-2">
                                                    <span class="badge bg-warning text-dark">
                                                        <i class="bi bi-star-fill"></i> ${bid.freelancerRating}
                                                    </span>
                                                    <c:choose>
                                                        <c:when test="${bid.status eq 'pending'}">
                                                            <span class="badge bg-warning bg-opacity-25 text-warning">
                                                                <i class="bi bi-clock"></i> 待审核
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${bid.status eq 'accepted'}">
                                                            <span class="badge bg-success bg-opacity-25 text-success">
                                                                <i class="bi bi-check-circle"></i> 已中标
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${bid.status eq 'rejected'}">
                                                            <span class="badge bg-danger bg-opacity-25 text-danger">
                                                                <i class="bi bi-x-circle"></i> 未中标
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary bg-opacity-25 text-secondary">
                                                                ${bid.status}
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="text-end">
                                            <h5 class="text-primary mb-0">¥${bid.amount}</h5>
                                            <small class="text-muted">${bid.days} 天</small>
                                        </div>
                                    </div>

                                    <div class="mt-3 p-3 bg-light rounded-3">
                                        <p class="mb-0 text-secondary">${bid.proposal}</p>
                                    </div>

                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                        <small class="text-muted">
                                            <i class="bi bi-clock-history"></i>
                                            提交于 ${bid.createdAt}
                                        </small>

                                        <c:if test="${bid.status eq 'pending'}">
                                            <form action="${pageContext.request.contextPath}/bid/award"
                                                  method="post" class="d-inline"
                                                  onsubmit="return confirm('确定授标给 ${bid.freelancerName}？此操作不可撤回。');">
                                                <input type="hidden" name="bidId" value="${bid.id}">
                                                <button type="submit" class="btn btn-success btn-sm">
                                                    <i class="bi bi-trophy"></i> 授标
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${bid.status eq 'accepted'}">
                                            <span class="text-success fw-semibold">
                                                <i class="bi bi-check-circle-fill"></i> 已授标
                                            </span>
                                        </c:if>
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
