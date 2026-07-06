<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="chen_zi_hao.Order, chen_zi_hao.Review, chen_zi_hao.User, java.util.List" %>
<%
    String ctx = request.getContextPath();
    Order od = (Order) request.getAttribute("order");
    if (od == null) { response.sendRedirect(ctx + "/orders"); return; }
    List<Review> odReviews = (List<Review>) request.getAttribute("reviews");
    User odUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单详情 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>
    <div class="container py-4">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h4><%= od.getProjectTitle() != null ? od.getProjectTitle() : "订单#" + od.getId() %></h4>
                <span class="badge <%= "in_progress".equals(od.getStatus()) ? "bg-warning text-dark" : ("awaiting_confirm".equals(od.getStatus()) ? "bg-info text-dark" : "bg-success") %>"><%= od.getStatus() %></span>
                <hr>
                <div class="row">
                    <div class="col-md-4"><strong>金额：</strong>&yen;<%= String.format("%.2f", od.getAmount()) %></div>
                    <div class="col-md-4"><strong>雇主：</strong><%= od.getEmployerName() %></div>
                    <div class="col-md-4"><strong>自由职业者：</strong><%= od.getFreelancerName() %></div>
                </div>
                <% if (odUser != null) { %>
                    <hr>
                    <% if ("in_progress".equals(od.getStatus())) { %>
                        <form method="post" action="<%= ctx %>/order/complete" style="display:inline">
                            <input type="hidden" name="id" value="<%= od.getId() %>">
                            <button type="submit" class="btn btn-success btn-sm">标记完成</button>
                        </form>
                    <% } %>
                    <% if ("awaiting_confirm".equals(od.getStatus())) { %>
                        <form method="post" action="<%= ctx %>/order/confirm" style="display:inline">
                            <input type="hidden" name="id" value="<%= od.getId() %>">
                            <button type="submit" class="btn btn-gradient btn-sm">确认完成</button>
                        </form>
                    <% } %>
                    <% if ("completed".equals(od.getStatus())) { %>
                        <a href="<%= ctx %>/review?orderId=<%= od.getId() %>&toUserId=<%= od.getFreelancerId() %>" class="btn btn-outline-warning btn-sm">评价</a>
                    <% } %>
                <% } %>
            </div>
        </div>
        <% if (odReviews != null && !odReviews.isEmpty()) { %>
            <div class="card shadow-sm">
                <div class="card-header"><h5 class="mb-0">评价</h5></div>
                <div class="card-body">
                    <% for (Review r : odReviews) { %>
                        <div class="border-bottom pb-2 mb-2">
                            <strong><%= r.getFromUserName() %></strong>
                            <span class="text-warning"><% for(int i=0;i<r.getScore();i++){ %>★<% } %></span>
                            <p class="mb-0"><%= r.getComment() != null ? r.getComment() : "" %></p>
                        </div>
                    <% } %>
                </div>
            </div>
        <% } %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
