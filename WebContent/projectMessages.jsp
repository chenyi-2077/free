<%@ page import="java.util.List, com.freelite.model.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User loginUser = (User) session.getAttribute("user");
    if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<ProjectMessage> messages = (List<ProjectMessage>) request.getAttribute("messages");
    int projectId = (int) request.getAttribute("projectId");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>项目沟通 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f5f6fa; }
        .chat-container { max-width: 720px; margin: 0 auto; }
        .msg-bubble { border-radius: 16px; padding: 12px 16px; margin-bottom: 12px; max-width: 80%; }
        .msg-self { background: #667eea; color: white; margin-left: auto; border-bottom-right-radius: 4px; }
        .msg-other { background: white; border: 1px solid #e0e0e0; border-bottom-left-radius: 4px; }
        .msg-time { font-size: 0.7rem; opacity: 0.7; margin-top: 4px; }
        .msg-sender { font-size: 0.8rem; font-weight: 600; margin-bottom: 4px; }
        .card { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/tags/navbar.jsp" />

    <div class="container chat-container mt-4">
        <h4 class="fw-bold mb-4">💬 项目沟通</h4>
        <a href="<%= request.getContextPath() %>/project/<%= projectId %>" class="btn btn-sm btn-outline-secondary mb-3">← 返回项目详情</a>

        <div class="card p-4 mb-4" style="min-height: 300px; max-height: 500px; overflow-y: auto;">
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
                                <%= msg.getCreatedAt() != null ? msg.getCreatedAt().toString().replace("T", " ").substring(0, 19) : "" %>
                            </div>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>

        <div class="card p-4">
            <form action="<%= request.getContextPath() %>/project/message" method="post">
                <input type="hidden" name="projectId" value="<%= projectId %>">
                <div class="mb-3">
                    <textarea name="content" class="form-control" rows="3" placeholder="输入消息..." required></textarea>
                </div>
                <button type="submit" class="btn btn-primary w-100">发送消息</button>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
