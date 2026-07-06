<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, chen_xi_rui.Bid, chen_xi_rui.User" %>
<%

    List<Bid> bids = (List<Bid>) request.getAttribute("bids");
    Integer bpProjectId = (Integer) request.getAttribute("projectId");
    User bpUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>项目竞标 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body class="bg-light">
    <%@ include file="/navbar.jsp" %>
    <div class="container py-4">
        <h4 class="mb-4">项目竞标列表</h4>
        <a href="<%= ctx %>/bid/place?projectId=<%= bpProjectId %>" class="btn btn-gradient mb-3">提交竞标</a>
        <a href="<%= ctx %>/project/detail?id=<%= bpProjectId %>" class="btn btn-outline-secondary mb-3">返回项目详情</a>
        <% if (bids == null || bids.isEmpty()) { %>
            <p class="text-muted">暂无竞标</p>
        <% } else { %>
            <div class="row">
                <% for (Bid b : bids) { %>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <h6><%= b.getFreelancerName() != null ? b.getFreelancerName() : "未知" %></h6>
                                <p class="mb-1"><strong>报价：</strong>&yen;<%= String.format("%.2f", b.getAmount()) %></p>
                                <p class="mb-1"><strong>工期：</strong><%= b.getDays() %> 天</p>
                                <p class="mb-1"><strong>方案：</strong><%= b.getProposal() %></p>
                                <span class="badge <%= "pending".equals(b.getStatus()) ? "bg-warning" : "bg-success" %>"><%= b.getStatus() %></span>
                                <% if (bpUser != null && "pending".equals(b.getStatus())) { %>
                                    <form method="post" action="<%= ctx %>/bid/award" class="mt-2">
                                        <input type="hidden" name="bidId" value="<%= b.getId() %>">
                                        <button type="submit" class="btn btn-success btn-sm">授标</button>
                                    </form>
                                <% } %>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>
    </div>
</body>
</html>
