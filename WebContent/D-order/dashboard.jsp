<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.freelite.model.*" %>
<%
    User loginUser = (User) session.getAttribute("user");
    int totalOrders = (int) request.getAttribute("totalOrders");
    int completedOrders = (int) request.getAttribute("completedOrders");
    int inProgressOrders = (int) request.getAttribute("inProgressOrders");
    List<Order> recentOrders = (List<Order>) request.getAttribute("recentOrders");
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
        body { background: #f5f6fa; }
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
        .bg-orange { background: linear-gradient(135deg, #f093fb, #f5576c); }
        .bg-blue { background: linear-gradient(135deg, #4facfe, #00f2fe); }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/projects" style="color: #667eea;">Freelite</a>
            <div class="d-flex">
                <a href="<%= request.getContextPath() %>/projects" class="text-decoration-none text-muted me-3">项目</a>
                <a href="<%= request.getContextPath() %>/profile" class="text-decoration-none text-muted"><%= loginUser.getDisplayName() %></a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h4 class="fw-bold mb-4">📊 数据看板</h4>

        <%-- 统计卡片 --%>
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
            <%-- ECharts 柱状图 --%>
            <div class="col-md-6 mb-4">
                <div class="card-chart">
                    <h5 class="fw-bold mb-3">订单状态分布</h5>
                    <div id="chartStatus" style="height: 300px;"></div>
                </div>
            </div>

            <%-- 最近订单 --%>
            <div class="col-md-6 mb-4">
                <div class="card-chart">
                    <h5 class="fw-bold mb-3">最近订单</h5>
                    <% if (recentOrders == null || recentOrders.isEmpty()) { %>
                        <p class="text-muted">暂无订单</p>
                    <% } else { %>
                        <% for (Order o : recentOrders) { %>
                            <div class="d-flex justify-content-between align-items-center mb-2 pb-2 border-bottom">
                                <div>
                                    <small class="fw-bold"><%= o.getProjectTitle() %></small><br>
                                    <small class="text-muted">¥<%= String.format("%.0f", o.getAmount()) %></small>
                                </div>
                                <small>
                                    <% if ("completed".equals(o.getStatus())) { %>
                                        <span class="badge bg-success">完成</span>
                                    <% } else if ("in_progress".equals(o.getStatus())) { %>
                                        <span class="badge bg-warning text-dark">进行中</span>
                                    <% } else { %>
                                        <span class="badge bg-secondary">取消</span>
                                    <% } %>
                                </small>
                            </div>
                        <% } %>
                    <% } %>
                </div>
            </div>
        </div>

    </div>

    <script>
        // ECharts 柱状图
        var chart = echarts.init(document.getElementById('chartStatus'));
        chart.setOption({
            tooltip: { trigger: 'axis' },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: {
                type: 'category',
                data: ['进行中', '已完成', '已取消']
            },
            yAxis: { type: 'value' },
            series: [{
                type: 'bar',
                data: [
                    <%= inProgressOrders %>,
                    <%= completedOrders %>,
                    <%= totalOrders - inProgressOrders - completedOrders %>
                ],
                itemStyle: {
                    borderRadius: [8, 8, 0, 0],
                    color: [
                        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#667eea' }, { offset: 1, color: '#764ba2' }
                        ]),
                        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#11998e' }, { offset: 1, color: '#38ef7d' }
                        ]),
                        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#f093fb' }, { offset: 1, color: '#f5576c' }
                        ])
                    ]
                }
            }]
        });
    </script>
</body>
</html>
