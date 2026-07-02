<%@ page import="com.freelite.model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String orderId = request.getParameter("orderId");
    if (orderId == null || orderId.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/orders");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>提交评价 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        .star-rating {
            font-size: 2rem;
            cursor: pointer;
            display: flex;
            gap: 8px;
            direction: rtl;
        }
        .star-rating span {
            color: var(--okx-text-muted);
            transition: color 0.15s;
        }
        .star-rating span:hover,
        .star-rating span:hover ~ span,
        .star-rating.active span {
            color: var(--okx-yellow);
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <div class="d-flex gap-3">
                <a href="${pageContext.request.contextPath}/projects" class="nav-link">项目市场</a>
                <a href="${pageContext.request.contextPath}/orders" class="nav-link">订单</a>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">📝 提交评价</h5>
                    </div>
                    <div class="card-body">
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <form method="post" action="${pageContext.request.contextPath}/review">
                            <input type="hidden" name="orderId" value="<%= orderId %>">

                            <div class="mb-4">
                                <label class="form-label fw-semibold">评分</label>
                                <div class="star-rating" id="starRating">
                                    <span data-value="5">★</span>
                                    <span data-value="4">★</span>
                                    <span data-value="3">★</span>
                                    <span data-value="2">★</span>
                                    <span data-value="1">★</span>
                                </div>
                                <input type="hidden" name="score" id="scoreInput" value="5">
                                <small class="text-muted" id="scoreLabel">非常好 (5分)</small>
                            </div>

                            <div class="mb-4">
                                <label for="comment" class="form-label fw-semibold">评价内容</label>
                                <textarea name="comment" id="comment" class="form-control" rows="4" placeholder="说说你的体验..." required></textarea>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary">提交评价</button>
                                <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">返回</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const stars = document.querySelectorAll('#starRating span');
        const scoreInput = document.getElementById('scoreInput');
        const scoreLabel = document.getElementById('scoreLabel');
        const labels = ['', '很差 (1分)', '较差 (2分)', '一般 (3分)', '满意 (4分)', '非常好 (5分)'];

        stars.forEach(star => {
            star.addEventListener('mouseenter', function() {
                const val = parseInt(this.dataset.value);
                stars.forEach(s => {
                    s.style.color = parseInt(s.dataset.value) <= val ? '#f5b342' : '#ddd';
                });
            });

            star.addEventListener('mouseleave', function() {
                const val = parseInt(scoreInput.value);
                stars.forEach(s => {
                    s.style.color = parseInt(s.dataset.value) <= val ? '#f5b342' : '#ddd';
                });
            });

            star.addEventListener('click', function() {
                const val = parseInt(this.dataset.value);
                scoreInput.value = val;
                scoreLabel.textContent = labels[val];
                stars.forEach(s => {
                    s.style.color = parseInt(s.dataset.value) <= val ? '#f5b342' : '#ddd';
                });
            });
        });
    </script>
</body>
</html>
