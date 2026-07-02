<%@ page import="java.util.List, java.time.LocalDateTime, java.time.format.DateTimeFormatter, com.freelite.model.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    
    int projectId = (int) request.getAttribute("projectId");
    List<ProjectMessage> messages = (List<ProjectMessage>) request.getAttribute("messages");
    List<Delivery> deliveries = (List<Delivery>) request.getAttribute("deliveries");
    String projectTitle = (String) request.getAttribute("projectTitle");
    String successMsg = (String) session.getAttribute("successMsg");
    if (successMsg != null) { session.removeAttribute("successMsg"); }
    String errorMsg = (String) session.getAttribute("errorMsg");
    if (errorMsg != null) { session.removeAttribute("errorMsg"); }
    Project project = (Project) request.getAttribute("project");
    boolean isEmployer = project != null && loginUser.getId() == project.getEmployerId();
    boolean isFreelancer = project != null && !isEmployer;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>交付与沟通 - <%= projectTitle %> - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f5f6fa; }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .msg-bubble { border-radius: 16px; padding: 12px 16px; margin-bottom: 10px; max-width: 80%; }
        .msg-self { background: #667eea; color: white; margin-left: auto; border-bottom-right-radius: 4px; }
        .msg-other { background: white; border: 1px solid #e0e0e0; border-bottom-left-radius: 4px; }
        .msg-time { font-size: 0.7rem; opacity: 0.7; margin-top: 4px; }
        .msg-sender { font-size: 0.8rem; font-weight: 600; margin-bottom: 4px; }
        .delivery-file { background: #f8f9fa; border-radius: 8px; padding: 10px; }
        .tab-btn { border-radius: 8px; font-weight: 600; }
        .tab-btn.active { background: #667eea; color: white; border-color: #667eea; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />

    <div class="container mt-4" style="max-width: 780px;">
        <% if (successMsg != null) { %>
            <div class="alert alert-success alert-dismissible fade show"><%= successMsg %><button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        <% } %>
        <% if (errorMsg != null) { %>
            <div class="alert alert-danger alert-dismissible fade show"><%= errorMsg %><button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        <% } %>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="fw-bold mb-0">📦 交付与沟通</h4>
            <div>
                <a href="<%= request.getContextPath() %>/project/<%= projectId %>" class="btn btn-sm btn-outline-secondary">项目详情</a>
                <a href="<%= request.getContextPath() %>/orders" class="btn btn-sm btn-outline-secondary">我的订单</a>
            </div>
        </div>
        <p class="text-muted"><%= projectTitle %></p>

        <%-- Tab 切换 --%>
        <ul class="nav nav-pills mb-3" id="deliveryTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active tab-btn" id="chat-tab" data-bs-toggle="pill" data-bs-target="#chat" type="button">💬 沟通</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link tab-btn" id="delivery-tab" data-bs-toggle="pill" data-bs-target="#delivery" type="button">📎 交付物</button>
            </li>
        </ul>

        <div class="tab-content">
            <%-- Tab1: 沟通 --%>
            <div class="tab-pane fade show active" id="chat" role="tabpanel">
                <div class="card p-3 mb-3" style="min-height: 300px; max-height: 450px; overflow-y: auto;">
                    <% if (messages == null || messages.isEmpty()) { %>
                        <p class="text-muted text-center my-5">暂无消息，发送第一条消息吧</p>
                    <% } else { %>
                        <% for (ProjectMessage msg : messages) {
                            boolean isSelf = msg.getSenderId() == loginUser.getId();
                        %>
                            <div class="d-flex <%= isSelf ? "justify-content-end" : "" %>">
                                <div class="msg-bubble <%= isSelf ? "msg-self" : "msg-other" %>">
                                    <div class="msg-sender"><%= isSelf ? "我" : msg.getSenderName() %></div>
                                    <div><%= msg.getContent() %></div>
                                    <div class="msg-time text-end">
                                        <%= msg.getCreatedAt() != null ? msg.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "" %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    <% } %>
                </div>
                <div class="card p-3">
                    <form action="<%= request.getContextPath() %>/deliveryChat" method="post">
                        <input type="hidden" name="projectId" value="<%= projectId %>">
                        <input type="hidden" name="action" value="message">
                        <div class="mb-2">
                            <textarea name="content" class="form-control" rows="2" placeholder="输入消息..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">发送消息</button>
                    </form>
                </div>
            </div>

            <%-- Tab2: 交付物 --%>
            <div class="tab-pane fade" id="delivery" role="tabpanel">
                <%-- 交付物列表 --%>
                <div class="card p-3 mb-3">
                    <h5 class="fw-bold mb-3">已上传的交付物</h5>
                    <% if (deliveries == null || deliveries.isEmpty()) { %>
                        <p class="text-muted text-center my-3">暂无交付物</p>
                    <% } else { %>
                        <% for (Delivery d : deliveries) { %>
                            <div class="delivery-file mb-2">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <strong><%= d.getTitle() != null && !d.getTitle().isEmpty() ? d.getTitle() : "交付物 #" + d.getId() %></strong><br>
                                        <small class="text-muted">
                                            上传者：<%= d.getUserName() %> | 
                                            <%= d.getCreatedAt() != null ? d.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "" %>
                                        </small>
                                    </div>
                                    <% if (d.getFileName() != null && !d.getFileName().isEmpty()) { %>
                                        <a href="<%= request.getContextPath() %>/file/download?id=<%= d.getId() %>" class="btn btn-sm btn-outline-primary">
                                            ⬇ <%= d.getFileName() %>
                                        </a>
                                    <% } %>
                                </div>
                                <% if (d.getDescription() != null && !d.getDescription().isEmpty()) { %>
                                    <p class="mt-2 mb-0" style="font-size: 0.9rem;"><%= d.getDescription() %></p>
                                <% } %>
                            </div>
                        <% } %>
                    <% } %>
                </div>

                <%-- 上传交付物（仅自由职业者/中标者） --%>
                <% if (!isEmployer) { %>
                    <div class="card p-3">
                        <h5 class="fw-bold mb-3">📤 上传交付物</h5>
                        <form action="<%= request.getContextPath() %>/deliveryChat" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="projectId" value="<%= projectId %>">
                            <input type="hidden" name="action" value="upload">
                            <div class="mb-2">
                                <input type="text" name="title" class="form-control" placeholder="交付标题（如：V1.0 源码）">
                            </div>
                            <div class="mb-2">
                                <textarea name="description" class="form-control" rows="2" placeholder="交付说明..."></textarea>
                            </div>
                            <div class="mb-2">
                                <input type="file" name="file" class="form-control" required>
                            </div>
                            <button type="submit" class="btn btn-success w-100">📤 上传交付物</button>
                        </form>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
