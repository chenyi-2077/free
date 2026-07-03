<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>提交竞标 - FreeLite</title>
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/bids?projectId=${projectId}">
                            <i class="bi bi-list-ul"></i> 竞标列表
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
        <div class="row justify-content-center">
            <div class="col-lg-8">

                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom-0 pt-4 pb-0">
                        <h4 class="card-title mb-0">
                            <i class="bi bi-send-plus text-primary"></i> 提交竞标
                        </h4>
                        <p class="text-muted small mt-1">项目 #${projectId}</p>
                    </div>
                    <div class="card-body px-4 pb-4">

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger d-flex align-items-center" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                ${error}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/bid/place" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="projectId" value="${projectId}">

                            <div class="row g-3">

                                <div class="col-md-6">
                                    <label for="amount" class="form-label fw-semibold">
                                        <i class="bi bi-currency-dollar text-primary"></i> 报价金额（元）
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text">¥</span>
                                        <input type="number" class="form-control" id="amount" name="amount"
                                               step="0.01" min="0" required placeholder="请输入报价">
                                        <span class="input-group-text">.00</span>
                                        <div class="invalid-feedback">请输入有效的报价金额</div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label for="days" class="form-label fw-semibold">
                                        <i class="bi bi-calendar-check text-primary"></i> 工期（天数）
                                    </label>
                                    <div class="input-group">
                                        <input type="number" class="form-control" id="days" name="days"
                                               min="1" required placeholder="预计完成天数">
                                        <span class="input-group-text">天</span>
                                        <div class="invalid-feedback">请输入有效的工期天数</div>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <label for="proposal" class="form-label fw-semibold">
                                        <i class="bi bi-file-text text-primary"></i> 方案描述
                                    </label>
                                    <textarea class="form-control" id="proposal" name="proposal"
                                              rows="6" required placeholder="请详细描述您的方案、技术路线和交付计划…"
                                              style="resize: vertical;"></textarea>
                                    <div class="invalid-feedback">请输入方案描述</div>
                                </div>

                            </div>

                            <hr class="my-4">

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/bids?projectId=${projectId}"
                                   class="btn btn-outline-secondary">
                                    <i class="bi bi-arrow-left"></i> 返回
                                </a>
                                <button type="submit" class="btn btn-primary px-4">
                                    <i class="bi bi-send"></i> 提交竞标
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        (function () {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
