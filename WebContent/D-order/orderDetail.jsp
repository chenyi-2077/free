<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    Order order = (Order) request.getAttribute("order");
    boolean canReview = (boolean) request.getAttribute("canReview");
    boolean isEmployer = (boolean) request.getAttribute("isEmployer");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单详情 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .btn-primary {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none; border-radius: 8px; padding: 10px 24px; font-weight: 600;
        }
        .star-rating .bi-star-fill { color: #ffc107; }
        .star-rating .bi-star { color: #ddd; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/orders" style="color: #667eea;">Freelite</a>
            <a href="<%= request.getContextPath() %>/orders" class="text-decoration-none text-muted">← 返回订单列表</a>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">

                <div class="card p-4">
                    <div class="d-flex justify-content-between align-items-start">
                        <h4 class="fw-bold"><%= order.getProjectTitle() %></h4>
                        <% if ("in_progress".equals(order.getStatus())) { %>
                            <span class="badge bg-warning text-dark fs-6">进行中</span>
                        <% } else if ("completed".equals(order.getStatus())) { %>
                            <span class="badge bg-success fs-6">已完成 ✓</span>
                        <% } %>
                    </div>
                    <hr>

                    <p><i class="bi bi-person"></i> <strong>雇主：</strong><%= order.getEmployerName() %></p>
                    <p><i class="bi bi-person-gear"></i> <strong>自由职业者：</strong><%= order.getFreelancerName() %></p>
                    <p><i class="bi bi-coin"></i> <strong>金额：</strong><span style="color: #28a745;">¥<%= String.format("%.0f", order.getAmount()) %></span></p>
                    <p><i class="bi bi-calendar"></i> <strong>创建时间：</strong><%= order.getCreatedAt() %></p>

                    <hr>

                    <%-- 完成按钮（自由职业者） --%>
                    <% if ("in_progress".equals(order.getStatus()) && !isEmployer) { %>
                        <form action="<%= request.getContextPath() %>/order/complete" method="post" class="mb-3">
                            <input type="hidden" name="orderId" value="<%= order.getId() %>">
                            <button type="submit" class="btn btn-success w-100">✅ 标记为已完成</button>
                        </form>
                    <% } %>

                    <%-- 评价表单 --%>
                    <% if (canReview) { %>
                        <div class="card p-3 mt-3" style="background: #f8f9ff;">
                            <h5 class="fw-bold mb-3">⭐ 评价</h5>
                            <form action="<%= request.getContextPath() %>/review" method="post">
                                <input type="hidden" name="orderId" value="<%= order.getId() %>">

                                <div class="mb-3">
                                    <label class="form-label fw-bold">评分</label>
                                    <div class="star-rating">
                                        <select name="score" class="form-select">
                                            <option value="5">5 - 非常满意</option>
                                            <option value="4">4 - 满意</option>
                                            <option value="3">3 - 一般</option>
                                            <option value="2">2 - 不满意</option>
                                            <option value="1">1 - 非常糟糕</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label fw-bold">评价内容</label>
                                    <textarea name="comment" class="form-control" rows="3" placeholder="说说你的体验..."></textarea>
                                </div>

                                <button type="submit" class="btn btn-primary w-100">提交评价</button>
                            </form>
                        </div>
                    <% } %>

                </div>

            </div>
        </div>
    </div>
</body>
</html>
