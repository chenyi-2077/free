<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    Project project = (Project) request.getAttribute("project");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>投递竞标 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .btn-primary {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            border-radius: 8px; padding: 12px; font-weight: 600;
        }
        .form-control, textarea { border-radius: 8px; padding: 12px; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: #667eea;">Freelite</a>
            <a href="<%= request.getContextPath() %>/project/<%= project.getId() %>" class="text-decoration-none text-muted">← 返回项目详情</a>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">

                <div class="card p-4">
                    <h4 class="fw-bold mb-2">📩 投递竞标</h4>
                    <p class="text-muted mb-4">
                        项目：<strong><%= project.getTitle() %></strong>
                        （预算 ¥<%= String.format("%.0f", project.getBudget()) %>）
                    </p>

                    <% if (error != null) { %>
                        <div class="alert alert-warning"><%= error %></div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/bid/place" method="post">
                        <input type="hidden" name="projectId" value="<%= project.getId() %>">

                        <div class="mb-3">
                            <label class="form-label fw-bold">你的报价（¥）</label>
                            <input type="number" name="amount" class="form-control" placeholder="例如：3000" required step="0.01">
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">预计完成天数</label>
                            <input type="number" name="days" class="form-control" placeholder="例如：15" required min="1">
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">方案说明</label>
                            <textarea name="proposal" class="form-control" rows="5" placeholder="说明你的技术方案、相关经验..."></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">提交竞标</button>
                    </form>
                </div>

            </div>
        </div>
    </div>
</body>
</html>
