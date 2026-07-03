<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.freelite.model.Order" %>
<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单列表 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="navbar.jsp" %>
    <div class="container py-4">
        <h4 class="mb-4">订单列表</h4>
        <div class="row">
            <% if (orders != null) for (Order o : orders) { %>
                <div class="col-md-6 mb-3">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h5><a href="${pageContext.request.contextPath}/order/detail?id=<%= o.getId() %>"><%= o.getProjectTitle() != null ? o.getProjectTitle() : "订单#" + o.getId() %></a></h5>
                            <p><strong>金额：</strong>¥<%= String.format("%.2f", o.getAmount()) %></p>
                            <p><strong>雇主：</strong><%= o.getEmployerName() %> <strong>自由职业者：</strong><%= o.getFreelancerName() %></p>
                            <span class="badge <%= "in_progress".equals(o.getStatus()) ? "bg-primary" : "bg-success" %>"><%= o.getStatus() %></span>
                        </div>
                    </div>
                </div>
            <% } %>
            <% if (orders == null || orders.isEmpty()) { %>
                <div class="col-12"><p class="text-muted">暂无订单</p></div>
            <% } %>
        </div>
    </div>
</body>
</html>
