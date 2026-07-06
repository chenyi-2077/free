<%@ page import="java.util.List, java.time.LocalDateTime, java.time.format.DateTimeFormatter, chen_yi_an.User, chen_yi_an.ProjectMessage, chen_yi_an.Delivery, chen_kai_bo.Project, chen_zi_hao.Order" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    
    Integer projectIdObj = (Integer) request.getAttribute("projectId");
    int projectId = projectIdObj != null ? projectIdObj : 0;
    List<ProjectMessage> messages = (List<ProjectMessage>) request.getAttribute("messages");
    List<Delivery> deliveries = (List<Delivery>) request.getAttribute("deliveries");
    String projectTitle = (String) request.getAttribute("projectTitle");
    String successMsg = (String) session.getAttribute("successMsg");
    if (successMsg != null) { session.removeAttribute("successMsg"); }
    String errorMsg = (String) session.getAttribute("errorMsg");
    if (errorMsg != null) { session.removeAttribute("errorMsg"); }
    Project project = (Project) request.getAttribute("project");
    Order order = (Order) request.getAttribute("order");
    String myRole = (String) request.getAttribute("myRole");
    if (myRole == null) myRole = "viewer";
    boolean isEmployer = "employer".equals(myRole);
    boolean isFreelancer = "freelancer".equals(myRole);

    // 进度计算
    String orderStatus = order != null ? order.getStatus() : (project != null ? project.getStatus() : "");
    int progressPct = 0;
    String progressLabel = "";
    if (order != null) {
        if ("in_progress".equals(order.getStatus())) { progressPct = 40; progressLabel = "⏳ 项目进行中"; }
        else if ("awaiting_confirm".equals(order.getStatus())) { progressPct = 70; progressLabel = "⏳ 等待雇主确认"; }
        else if ("completed".equals(order.getStatus())) { progressPct = 100; progressLabel = "✅ 项目已完成"; }
    } else if ("open".equals(project.getStatus())) { progressPct = 10; progressLabel = "📋 招募中"; }
    else if ("cancelled".equals(project.getStatus())) { progressPct = 0; progressLabel = "❌ 已取消"; }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>交付与沟通 - <%= projectTitle %> - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelite.css">
    <style>
        body { background: #ffffff; }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .msg-bubble { border-radius: 16px; padding: 12px 16px; margin-bottom: 10px; max-width: 80%; }
        .msg-self { background: var(--accent); color: white; margin-left: auto; border-bottom-right-radius: 4px; }
        .msg-other { background: white; border: 1px solid #e0e0e0; border-bottom-left-radius: 4px; }
        .msg-time { font-size: 0.7rem; opacity: 0.7; margin-top: 4px; }
        .msg-sender { font-size: 0.8rem; font-weight: 600; margin-bottom: 4px; }
        .delivery-file { background: #f8f9fa; border-radius: 8px; padding: 10px; }
        .tab-btn { border-radius: 8px; font-weight: 600; }
        .tab-btn.active { background: var(--accent); color: white; border-color: var(--accent); }
        .progress-bar-custom { background: #d1fae5; border-radius: 8px; height: 12px; overflow: hidden; }
        .progress-bar-fill { height: 100%; background: var(--accent); border-radius: 8px; transition: width .4s; }
        .action-btn { border: none; border-radius: 10px; padding: 10px 20px; font-weight: 600; font-size: 0.9rem; cursor: pointer; transition: all .15s; }
        .action-btn:disabled { opacity: .5; cursor: not-allowed; }
        .action-btn.upload { background: #d1fae5; color: #059669; }
        .action-btn.upload:hover { background: #a7f3d0; }
        .action-btn.complete { background: #fef3c7; color: #d97706; }
        .action-btn.complete:hover { background: #fde68a; }
        .action-btn.confirm { background: #dbeafe; color: #2563eb; }
        .action-btn.confirm:hover { background: #bfdbfe; }
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

        <div class="d-flex justify-content-between align-items-center mb-2">
            <h4 class="fw-bold mb-0">📦 交付与沟通</h4>
            <div>
                <a href="<%= request.getContextPath() %>/project/<%= projectId %>" class="btn btn-sm btn-outline-secondary">项目详情</a>
                <a href="<%= request.getContextPath() %>/orders" class="btn btn-sm btn-outline-secondary">我的订单</a>
            </div>
        </div>
        <p class="text-muted mb-3"><%= projectTitle %></p>

        <%-- 进度条 --%>
        <% if (progressPct > 0 || "open".equals(project.getStatus())) { %>
        <div class="card p-3 mb-3" style="background: #f0fdf4;">
            <div class="progress-bar-custom mb-2">
                <div class="progress-bar-fill" style="width: <%= progressPct %>%"></div>
            </div>
            <div class="d-flex justify-content-between align-items-center">
                <span style="font-size: 0.85rem; font-weight: 600; color: #059659;"><%= progressLabel %></span>
                <span style="font-size: 0.8rem; color: #6b7280;">
                    <% if (order != null) { %>
                        <% if ("in_progress".equals(order.getStatus())) { %>已托管 ¥<%= String.format("%.2f", order.getEscrowAmount()) %>
                        <% } else if ("awaiting_confirm".equals(order.getStatus())) { %>¥<%= String.format("%.2f", order.getEscrowAmount()) %> 待释放
                        <% } else if ("completed".equals(order.getStatus())) { %>已结算 ¥<%= String.format("%.2f", order.getAmount()) %>
                        <% } %>
                    <% } else if ("open".equals(project.getStatus())) { %>等待竞标中<% } %>
                </span>
            </div>
        </div>
        <% } %>

        <%-- 操作按钮 --%>
        <% if (order != null) { %>
        <div class="card p-3 mb-3">
            <div class="d-flex gap-2">
                <% if ("in_progress".equals(order.getStatus()) && isFreelancer) { %>
                    <button type="button" class="action-btn complete" onclick="pageMarkComplete(<%= projectId %>)">✅ 标记完成</button>
                <% } %>
                <% if ("awaiting_confirm".equals(order.getStatus()) && isEmployer) { %>
                    <button type="button" class="action-btn confirm" onclick="pageConfirmComplete(<%= projectId %>)">✅ 确认完成并释放资金</button>
                <% } %>
                <% if ("awaiting_confirm".equals(order.getStatus()) && isFreelancer) { %>
                    <span style="font-size: 0.85rem; color: #d97706; padding: 10px 0;">⏳ 已标记完成，等待雇主确认中...</span>
                <% } %>
                <% if ("completed".equals(order.getStatus())) { %>
                    <span style="font-size: 0.85rem; color: #059669; padding: 10px 0;">✅ 项目已完成，资金已结算</span>
                <% } %>
            </div>
        </div>
        <% } %>

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
                    <h5 class="fw-bold mb-3">📎 已上传的交付物（<%= deliveries != null ? deliveries.size() : 0 %>）</h5>
                    <% if (deliveries == null || deliveries.isEmpty()) { %>
                        <p class="text-muted text-center my-3">暂无交付物</p>
                    <% } else { %>
                        <% for (Delivery d : deliveries) { %>
                            <div class="delivery-file mb-2">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <strong><%= d.getTitle() != null && !d.getTitle().isEmpty() ? d.getTitle() : "交付物 #" + d.getId() %></strong><br>
                                        <small class="text-muted">
                                            👤 <%= d.getUserName() %> · 
                                            <%= d.getCreatedAt() != null ? d.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "" %>
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

                <%-- 上传交付物（仅自由职业者） --%>
                <% if (isFreelancer && order != null && "in_progress".equals(order.getStatus())) { %>
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
    <script>
    // 页面上的标记完成（直接跳转提交）
    function pageMarkComplete(pid) {
        if (!confirm('确定标记项目已完成？标记后将等待雇主确认。')) return;
        var fd = new FormData();
        fd.append('projectId', pid);
        fd.append('action', 'complete');
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '<%= request.getContextPath() %>/api/chatDelivery', true);
        xhr.onload = function() {
            if (xhr.status === 200) {
                try {
                    var d = JSON.parse(xhr.responseText);
                    if (d.success) { alert('✅ 已标记完成，等待雇主确认'); location.reload(); }
                    else { alert(d.error || '操作失败'); }
                } catch(e) { alert('操作失败'); }
            } else { alert('操作失败'); }
        };
        xhr.send(fd);
    }
    function pageConfirmComplete(pid) {
        if (!confirm('确认完成后，托管资金将释放到自由职业者钱包。确认吗？')) return;
        var fd = new FormData();
        fd.append('projectId', pid);
        fd.append('action', 'confirm');
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '<%= request.getContextPath() %>/api/chatDelivery', true);
        xhr.onload = function() {
            if (xhr.status === 200) {
                try {
                    var d = JSON.parse(xhr.responseText);
                    if (d.success) { alert('✅ 已确认完成，资金已释放！'); location.reload(); }
                    else { alert(d.error || '操作失败'); }
                } catch(e) { alert('操作失败'); }
            } else { alert('操作失败'); }
        };
        xhr.send(fd);
    }
    </script>
<jsp:include page="/WEB-INF/tags/chatWidget.jsp" />
</body>
</html>
