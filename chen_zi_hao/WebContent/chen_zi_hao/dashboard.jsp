<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, chen_zi_hao.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    int totalOrders = request.getAttribute("totalOrders") != null ? ((Number) request.getAttribute("totalOrders")).intValue() : 0;
    int completedOrders = request.getAttribute("completedOrders") != null ? ((Number) request.getAttribute("completedOrders")).intValue() : 0;
    int inProgressOrders = request.getAttribute("inProgressOrders") != null ? ((Number) request.getAttribute("inProgressOrders")).intValue() : 0;
    List<Order> recentOrders = (List) request.getAttribute("recentOrders") != null ? (List) request.getAttribute("recentOrders") : new java.util.ArrayList();
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>数据看板 - Freelite</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.5.0/dist/echarts.min.js"></script>
    <style>
        .navbar { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .stat-card {
            border: none;
            border-radius: 16px;
            padding: 1.5rem;
            color: white;
        }
        .stat-card .number { font-size: 2.5rem; font-weight: 700; }
        .stat-card .label { opacity: 0.9; }
        .card-chart { border: none; border-radius: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); padding: 1.5rem; background: white; }
        .bg-purple { background: linear-gradient(135deg, #667eea, #764ba2); }
        .bg-green { background: linear-gradient(135deg, #11998e, #38ef7d); }
        .bg-orange { background: linear-gradient(135deg, #F6465D, #FF6B81); }
        .bg-blue { background: linear-gradient(135deg, #3498db, #67b8f7); }
    </style>
</head>
<body>
    <%@ include file="/navbar.jsp" %>

    <div class="container mt-4">
        <h4 class="fw-bold mb-4">📊 数据看板</h4>

        <% if (loginUser == null) { %>
            <div class="alert alert-info">请先<a href="<%= ctx %>/login" class="alert-link">登录</a>以查看数据看板</div>
        <% } else { %>

        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="stat-card bg-purple">
                    <div class="label">总订单数</div>
                    <div class="number"><%= totalOrders %></div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stat-card bg-green">
                    <div class="label">已完成</div>
                    <div class="number"><%= completedOrders %></div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stat-card bg-orange">
                    <div class="label">进行中</div>
                    <div class="number"><%= inProgressOrders %></div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stat-card bg-blue">
                    <div class="label">完成率</div>
                    <div class="number">
                        <%= totalOrders > 0 ? String.format("%.0f", (double) completedOrders / totalOrders * 100) : 0 %>%
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6 mb-4">
                <div class="card-chart">
                    <h5 class="fw-bold mb-3">订单状态分布</h5>
                    <div id="chartStatus" style="height: 300px;"></div>
                </div>
            </div>

            <div class="col-md-6 mb-4">
                <div class="card-chart">
                    <h5 class="fw-bold mb-3">最近订单</h5>
                    <% if (recentOrders == null || recentOrders.isEmpty()) { %>
                        <p class="text-muted">暂无订单</p>
                    <% } else { %>
                        <% for (Order o : recentOrders) { %>
                            <div class="d-flex justify-content-between align-items-center mb-2 pb-2 border-bottom">
                                <div>
                                    <small class="fw-bold"><%= o.getProjectTitle() != null ? o.getProjectTitle() : "项目#" + o.getProjectId() %></small><br>
                                    <small class="text-muted">¥<%= String.format("%.0f", o.getAmount()) %></small>
                                </div>
                                <small>
                                    <% if ("completed".equals(o.getStatus())) { %>
                                        <span class="badge bg-success">完成</span>
                                    <% } else if ("in_progress".equals(o.getStatus())) { %>
                                        <span class="badge bg-warning text-dark">进行中</span>
                                    <% } else if ("awaiting_confirm".equals(o.getStatus())) { %>
                                        <span class="badge bg-info">待确认</span>
                                    <% } else { %>
                                        <span class="badge bg-secondary"><%= o.getStatus() %></span>
                                    <% } %>
                                </small>
                            </div>
                        <% } %>
                    <% } %>
                </div>
            </div>
        </div>

        <% } %>
    </div>

    <script>
        var chart = echarts.init(document.getElementById('chartStatus'));
        chart.setOption({
            tooltip: { trigger: 'axis' },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: {
                type: 'category',
                data: ['进行中', '已完成', '待确认']
            },
            yAxis: { type: 'value' },
            series: [{
                type: 'bar',
                data: [<%= inProgressOrders %>, <%= completedOrders %>, <%= totalOrders - inProgressOrders - completedOrders %>],
                itemStyle: {
                    borderRadius: [8, 8, 0, 0],
                    color: [
                        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#00C897' }, { offset: 1, color: '#00DBA3' }
                        ]),
                        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#11998e' }, { offset: 1, color: '#38ef7d' }
                        ]),
                        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#F6465D' }, { offset: 1, color: '#FF6B81' }
                        ])
                    ]
                }
            }]
        });
    </script>
</body>
</html>
