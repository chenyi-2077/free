<%@ page import="java.util.List, com.freelite.model.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    Wallet wallet = (Wallet) request.getAttribute("wallet");
    List<TransactionLog> logs = (List<TransactionLog>) request.getAttribute("logs");
    String successMsg = (String) session.getAttribute("successMsg");
    if (successMsg != null) { session.removeAttribute("successMsg"); }
    String errorMsg = (String) session.getAttribute("errorMsg");
    if (errorMsg != null) { session.removeAttribute("errorMsg"); }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的钱包 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .balance-card {
            background: var(--accent);
            color: white;
            border-radius: 16px;
            padding: 2rem;
        }
        .balance-amount { font-size: 2.5rem; font-weight: 700; }
        .balance-label { opacity: 0.8; font-size: 0.9rem; }
        .type-badge { font-size: 0.75rem; padding: 2px 8px; border-radius: 12px; }
        .type-recharge { background: #d4edda; color: #155724; }
        .type-freeze { background: #fff3cd; color: #856404; }
        .type-release { background: #d1ecf1; color: #0c5460; }
        .type-refund { background: #e2e3e5; color: #383d41; }
        .type-income { background: #cce5ff; color: #004085; }
        .type-withdraw { background: #f8d7da; color: #721c24; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />

    <div class="container mt-4" style="max-width: 720px;">
        <% if (successMsg != null) { %>
            <div class="alert alert-success alert-dismissible fade show"><%= successMsg %><button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        <% } %>
        <% if (errorMsg != null) { %>
            <div class="alert alert-danger alert-dismissible fade show"><%= errorMsg %><button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        <% } %>

        <h4 class="fw-bold mb-4">💰 我的钱包</h4>

        <div class="balance-card mb-4">
            <div class="row text-center">
                <div class="col-4">
                    <div class="balance-label">可用余额</div>
                    <div class="balance-amount">¥<%= String.format("%.2f", wallet.getBalance()) %></div>
                </div>
                <div class="col-4">
                    <div class="balance-label">托管冻结</div>
                    <div class="balance-amount">¥<%= String.format("%.2f", wallet.getFrozen()) %></div>
                </div>
                <div class="col-4">
                    <div class="balance-label">总资产</div>
                    <div class="balance-amount">¥<%= String.format("%.2f", wallet.getBalance() + wallet.getFrozen()) %></div>
                </div>
            </div>
        </div>

        <%-- 充值 --%>
        <div class="card p-4 mb-4">
            <h5 class="fw-bold">充值</h5>
            <form action="<%= request.getContextPath() %>/recharge" method="post" class="row g-2 mt-2">
                <div class="col-md-8">
                    <input type="number" name="amount" class="form-control" step="0.01" min="1" placeholder="输入充值金额 (¥)" required>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-success w-100">💳 充值</button>
                </div>
            </form>
        </div>

        <%-- 交易流水 --%>
        <div class="card p-4">
            <h5 class="fw-bold mb-3">交易流水</h5>
            <% if (logs == null || logs.isEmpty()) { %>
                <p class="text-muted">暂无交易记录</p>
            <% } else { %>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>类型</th>
                                <th>金额</th>
                                <th>描述</th>
                                <th>时间</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (TransactionLog log : logs) {
                                String typeClass = "type-" + log.getType();
                                String typeName;
                                String sign;
                                switch (log.getType()) {
                                    case "recharge": typeName = "充值"; sign = "+"; break;
                                    case "freeze": typeName = "托管冻结"; sign = "-"; break;
                                    case "release": typeName = "释放"; sign = ""; break;
                                    case "refund": typeName = "退款"; sign = "+"; break;
                                    case "income": typeName = "收入"; sign = "+"; break;
                                    case "withdraw": typeName = "提现"; sign = "-"; break;
                                    default: typeName = log.getType(); sign = "";
                                }
                            %>
                            <tr>
                                <td><span class="type-badge <%= typeClass %>"><%= typeName %></span></td>
                                <td class="fw-bold"><%= sign %>¥<%= String.format("%.2f", log.getAmount()) %></td>
                                <td><%= log.getDescription() != null ? log.getDescription() : "" %></td>
                                <td style="font-size: 0.85rem; color: #999;">
                                    <%= log.getCreatedAt() != null ? log.getCreatedAt().toString().replace("T", " ").substring(0, 19) : "" %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
