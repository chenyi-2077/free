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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
</head>
<body>
    <nav class="navbar navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/projects">Freelite</a>
            <a href="${pageContext.request.contextPath}/projects" class="nav-link">← 返回列表</a>
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

                    <form action="${pageContext.request.contextPath}/project/post" method="post">
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
                                <label class="form-label fw-bold">预算 (¥)</label>
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
                        <button type="submit" class="btn btn-gradient w-100">发布项目</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
