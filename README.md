# Freelite 🚀

> 自由职业者协作平台 — 轻量版 Freelancer  
> 项目口号：**让自由协作，如此简单**

## 项目简介

Freelite 是一个 B/S 架构的 Web 应用，对标 Freelancer.com 的核心功能。  
雇主可以发布项目，自由职业者可以竞标接单，完成工作后评价评分。  
——国内没有同类好产品，我们自己造。

## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | JSP + Bootstrap 5 + JavaScript |
| 后端 | Java Servlet + JDBC |
| 数据库 | MySQL 5.7+ |
| 服务器 | Apache Tomcat 8.5+ |
| IDE | Eclipse IDE 2026-03 |

## 团队分工

| 角色 | 成员 | 模块 | 主要文件 |
|---|---|---|---|
| ⭐ **A（组长）** | @chenyi-2077 | 用户系统 + 整合 | User.java, LoginServlet, ProfileServlet, login.jsp, register.jsp |
| **B** | 待定 | 项目发布与浏览 | Project.java, ProjectListServlet, projectList.jsp, postProject.jsp |
| **C** | 待定 | 竞标系统 | Bid.java, PlaceBidServlet, AwardBidServlet, bidForm.jsp |
| **D** | 待定 | 订单与评价 | Order.java, Review.java, DashboardServlet, dashboard.jsp |

## 快速开始

### 环境要求
- JDK 8+
- Apache Tomcat 8.5+
- MySQL 5.7+
- Eclipse IDE 2026-03（或任意支持 Dynamic Web Project 的 IDE）

### 运行步骤

1. **Clone 项目**
   ```bash
   git clone https://github.com/chenyi-2077/Freelite.git
   ```

2. **导入 Eclipse**
   - File → Import → General → Existing Projects into Workspace
   - 选择 Freelite 文件夹
   - 确保 Eclipse 已配置 Tomcat Server

3. **创建数据库**
   ```sql
   mysql -u root -p < docs/database.sql
   ```

4. **修改数据库配置**
   - 打开 `src/com/freelite/util/DBUtil.java`
   - 修改数据库 URL、用户名、密码

5. **Run on Server**
   - 右键项目 → Run As → Run on Server
   - 选择 Tomcat → 浏览器自动打开
   - 访问 `http://localhost:8080/Freelite/`

## 项目结构

```
Freelite/
├── README.md
├── docs/
│   └── database.sql        ← 数据库建表脚本
├── WebContent/
│   ├── WEB-INF/web.xml
│   ├── A-user/             ← 用户模块页面
│   ├── B-project/          ← 项目模块页面
│   ├── C-bid/              ← 竞标模块页面
│   └── D-order/            ← 订单模块页面
├── src/com/freelite/
│   ├── model/              ← 实体类
│   ├── dao/                ← 数据库访问层
│   ├── servlet/            ← 控制层
│   └── util/               ← 工具类
└── .gitignore
```
