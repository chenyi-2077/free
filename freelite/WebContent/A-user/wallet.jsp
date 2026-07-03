<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freelite.model.User, java.util.List, java.util.Map" %>
<%
    User walletUser = (User) session.getAttribute("user");
    if (walletUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    Double balance = (Double) request.getAttribute("balance");
    Double frozen = (Double) request.getAttribute("frozen");
    List<Map<String, Object>> transactions = (List<Map<String, Object>>) request.getAttribute("transactions");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>钱包 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>
    <div class="container py-4">
        <div class="row">
            <div class="col-md-4">
                <div class="card shadow-sm mb-3">
                    <div class="card-body text-center">
                        <h6 class="text-muted">可用余额</h6>
                        <h2 class="text-success">¥<%= String.format("%.2f", balance != null ? balance : 0.0) %></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm mb-3">
                    <div class="card-body text-center">
                        <h6 class="text-muted">冻结金额</h6>
                        <h2 class="text-warning">¥<%= String.format("%.2f", frozen != null ? frozen : 0.0) %></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm mb-3">
                    <div class="card-body text-center">
                        <h6 class="text-muted">可用总额</h6>
                        <h2 class="text-primary">¥<%= String.format("%.2f", (balance != null ? balance : 0.0) + (frozen != null ? frozen : 0.0)) %></h2>
                    </div>
                </div>
            </div>
        </div>
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h5>充值</h5>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                <% } %>
                <form method="post" action="${pageContext.request.contextPath}/recharge" class="row g-2">
                    <div class="col-auto">
                        <input type="number" name="amount" class="form-control" step="0.01" min="0.01" placeholder="金额" required>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-success">充值</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="card shadow-sm">
            <div class="card-header"><h5 class="mb-0">交易流水</h5></div>
            <div class="card-body">
                <table class="table table-sm">
                    <thead><tr><th>类型</th><th>金额</th><th>说明</th><th>时间</th></tr></thead>
                    <tbody>
                    <% if (transactions != null) for (Map<String, Object> txn : transactions) { %>
                        <tr>
                            <td><%= txn.get("type") %></td>
                            <td>¥<%= String.format("%.2f", txn.get("amount")) %></td>
                            <td><%= txn.get("description") %></td>
                            <td><%= txn.get("createdAt") %></td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
