<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, chen_xi_rui.Bid, chen_xi_rui.User" %>
<%

    User mbUser = (User) session.getAttribute("user");
    List<Bid> mbList = (List<Bid>) request.getAttribute("bids");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的竞标 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>
    <div class="container py-4">
        <h4>我的竞标记录</h4>
        <% if (mbList == null || mbList.isEmpty()) { %>
            <p class="text-muted">您还没有提交过竞标</p>
        <% } else { %>
            <div class="row">
                <% for (Bid b : mbList) { %>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <p><strong>报价：</strong>&yen;<%= String.format("%.2f", b.getAmount()) %></p>
                                <p><strong>工期：</strong><%= b.getDays() %> 天</p>
                                <p><strong>方案：</strong><%= b.getProposal() %></p>
                                <span class="badge <%= "pending".equals(b.getStatus()) ? "bg-warning" : "bg-success" %>"><%= b.getStatus() %></span>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>
    </div>
</body>
</html>
