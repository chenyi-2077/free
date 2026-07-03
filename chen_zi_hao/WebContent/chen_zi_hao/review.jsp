<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="chen_zi_hao.User" %>
<%
    String ctx = request.getContextPath();
    User rvUser = (User) session.getAttribute("user");
    if (rvUser == null) { response.sendRedirect(ctx + "/login"); return; }
    String rvOrderId = String.valueOf(request.getAttribute("orderId"));
    String rvToUserId = String.valueOf(request.getAttribute("toUserId"));
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>评价 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div class="container">
            <a class="navbar-brand" href="<%= ctx %>/">Freelite</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/orders">订单列表</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h4>提交评价</h4>
                        <form method="post" action="<%= ctx %>/review">
                            <input type="hidden" name="orderId" value="<%= rvOrderId %>">
                            <input type="hidden" name="toUserId" value="<%= rvToUserId %>">
                            <div class="mb-3">
                                <label class="form-label">评分</label>
                                <select name="score" class="form-select">
                                    <option value="5">★★★★★</option>
                                    <option value="4">★★★★☆</option>
                                    <option value="3">★★★☆☆</option>
                                    <option value="2">★★☆☆☆</option>
                                    <option value="1">★☆☆☆☆</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">评论</label>
                                <textarea name="comment" class="form-control" rows="4"></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">提交</button>
                            <a href="<%= ctx %>/order/detail?id=<%= rvOrderId %>" class="btn btn-outline-secondary">取消</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
