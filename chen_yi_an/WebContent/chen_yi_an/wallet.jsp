<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    List<Map<String, Object>> transactions = (List<Map<String, Object>>) request.getAttribute("transactions");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>钱包 - 自由人平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card shadow-sm mb-4">
                    <div class="card-body p-4">
                        <h3 class="card-title text-center mb-4">我的钱包</h3>
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger"><%= error %></div>
                        <% } %>
                        <div class="row text-center mb-4">
                            <div class="col-6">
                                <div class="p-3 bg-primary bg-opacity-10 rounded">
                                    <small class="text-muted">余额</small>
                                    <h4 class="text-primary mb-0">
                                        ¥<%= String.format("%.2f", request.getAttribute("balance") != null ? (double) request.getAttribute("balance") : 0.0) %>
                                    </h4>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="p-3 bg-warning bg-opacity-10 rounded">
                                    <small class="text-muted">冻结金额</small>
                                    <h4 class="text-warning mb-0">
                                        ¥<%= String.format("%.2f", request.getAttribute("frozen") != null ? (double) request.getAttribute("frozen") : 0.0) %>
                                    </h4>
                                </div>
                            </div>
                        </div>
                        <form action="${pageContext.request.contextPath}/recharge" method="post">
                            <div class="input-group mb-3">
                                <span class="input-group-text">¥</span>
                                <input type="number" class="form-control" name="amount" placeholder="充值金额" step="0.01" min="0.01" required>
                                <button type="submit" class="btn btn-success">充值</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="card shadow-sm">
                    <div class="card-body">
                        <h4 class="mb-3">交易流水</h4>
                        <%
                            if (transactions != null && !transactions.isEmpty()) {
                        %>
                        <div class="table-responsive">
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>金额</th>
                                        <th>类型</th>
                                        <th>描述</th>
                                        <th>时间</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <%
                                    for (Map<String, Object> txn : transactions) {
                                %>
                                    <tr>
                                        <td class="<%= "recharge".equals(txn.get("type")) ? "text-success" : "text-danger" %>">
                                            <%= "recharge".equals(txn.get("type")) ? "+" : "-" %>¥<%= String.format("%.2f", txn.get("amount")) %>
                                        </td>
                                        <td><%= txn.get("type") %></td>
                                        <td><%= txn.get("description") %></td>
                                        <td><small class="text-muted"><%= txn.get("createdAt") %></small></td>
                                    </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% } else { %>
                        <p class="text-muted text-center">暂无交易记录</p>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
