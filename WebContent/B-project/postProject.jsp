<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>发布项目 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f5f6fa; }
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 16px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .btn-primary {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            border-radius: 8px;
            padding: 12px;
            font-weight: 600;
        }
        .form-control, .form-select, textarea {
            border-radius: 8px;
            padding: 12px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: #667eea;">Freelite</a>
            <div class="d-flex align-items-center gap-3">
                <span class="text-muted"><%= loginUser.getDisplayName() != null ? loginUser.getDisplayName() : loginUser.getEmail() %></span>
                <a href="<%= request.getContextPath() %>/profile" class="text-decoration-none text-muted">个人主页</a>
                <a href="<%= request.getContextPath() %>/logout" class="text-decoration-none text-muted">退出</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">

                <div class="card p-4">
                    <h4 class="fw-bold mb-4">📋 发布新项目</h4>

                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger"><%= error %></div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/project/post" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-bold">项目标题 <span class="text-danger">*</span></label>
                            <input type="text" name="title" class="form-control" placeholder="例如：开发一个博客系统" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">项目描述</label>
                            <textarea name="description" class="form-control" rows="5" placeholder="详细描述你的需求..."></textarea>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="form-label fw-bold">预算（¥）</label>
                                <input type="number" name="budget" class="form-control" placeholder="5000" step="0.01">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">截止日期</label>
                                <input type="date" name="deadline" class="form-control">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">分类</label>
                                <select name="categoryId" class="form-select">
                                    <% for (Category c : categories) { %>
                                        <option value="<%= c.getId() %>"><%= c.getName() %></option>
                                    <% } %>
                                </select>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">发布项目</button>
                    </form>
                </div>

            </div>
        </div>
    </div>
</body>
</html>
