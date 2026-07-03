<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%
    String ctx = request.getContextPath();
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("stats");
    if (stats == null) { response.sendRedirect(ctx + "/orders"); return; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>数据看板 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div class="container">
            <a class="navbar-brand" href="<%= ctx %>/">Freelite</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/orders">订单列表</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/dashboard">数据看板</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container py-4">
        <h4 class="mb-4">数据看板</h4>
        <div class="row">
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <h6 class="text-muted">项目总数</h6>
                        <h2 class="text-primary"><%= stats.getOrDefault("projectCount", 0) %></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <h6 class="text-muted">订单总数</h6>
                        <h2 class="text-info"><%= stats.getOrDefault("orderCount", 0) %></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <h6 class="text-muted">评价总数</h6>
                        <h2 class="text-secondary"><%= stats.getOrDefault("reviewCount", 0) %></h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
