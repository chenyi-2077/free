<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="chen_xi_rui.User" %>
<%
    User idxUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>竞标系统 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/navbar.jsp" %>
    <div class="container mt-5 text-center">
        <h3 class="mb-4">📋 竞标系统</h3>
        <div class="row justify-content-center">
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5>浏览竞标</h5>
                        <p class="text-muted">查看项目竞标列表</p>
                        <a href="<%= ctx %>/bids" class="btn btn-outline-primary">进入</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5>我的竞标</h5>
                        <p class="text-muted">查看我提交的竞标</p>
                        <a href="<%= ctx %>/my/bids" class="btn btn-outline-success">进入</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
