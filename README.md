# Freelite 🚀

> 自由职业项目竞标平台 — 轻量版 Freelancer

---

## 📋 项目简介

Freelite 是一个 B/S 架构的 Web 应用，对标 Freelancer.com 的核心功能。

**技术栈**：JSP + Bootstrap 5 + Java Servlet + JDBC + MySQL 5.7 + Tomcat 8.5

---

## 👥 团队与分工

| 学号 | 姓名 | 程序模块 | 分包目录 |
|---|---|---|---|
| 24030505 | **陈怡安** | 用户注册/登录/认证/个人资料 | `A-package/` |
| 24030503 | **陈凯博** | 项目发布/列表/搜索/详情/编辑 | `B-package/` |
| 24030504 | **陈僖睿** | 竞标提交/列表/授标/我的竞标 | `C-package/` |
| 24030506 | **陈子豪** | 订单创建/管理/评价/数据看板 | `D-package/` |

---

## 🗂️ 包结构

每个分包是**独立的 Eclipse Dynamic Web Project**，可直接导入运行。

```
Freelite/
├── A-package/       ← 用户系统（陈怡安）
│   ├── src/         ← Java 源码
│   ├── WebContent/  ← JSP 页面
│   ├── lib/         ← MySQL Connector
│   ├── .project     ← Eclipse 项目配置
│   └── .classpath   ← 编译路径
├── B-package/       ← 项目模块（陈凯博）
├── C-package/       ← 竞标模块（陈僖睿）
├── D-package/       ← 订单模块（陈子豪）
├── full-package/    ← 完整整合版
├── docs/            ← 说明书、数据库脚本
└── README.md
```

---

## ⚙️ 快速开始

### 环境要求
- JDK 8+
- Apache Tomcat 8.5+
- MySQL 5.7+
- IDE：Eclipse 2026-03

### 运行步骤

**1. Clone 项目**
```bash
git clone https://github.com/chenyi-2077/free.git
```

**2. 创建数据库**
```bash
mysql -u root -p < docs/database.sql
```

**3. 修改数据库连接**
打开 `你的包目录/src/com/freelite/util/DBUtil.java`，修改：
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/freelite?...";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "你的密码";
```

**4. 导入 Eclipse**
File → Import → General → Existing Projects into Workspace
选择你自己的分包目录（如 `B-package/`）

**5. 运行**
右键项目 → Run As → Run on Server → 选择 Tomcat 8.5

> 每个分包已默认**注释掉登录验证过滤器**，可直接独立测试。
> 完整权限验证在 `full-package/`（整合版）中开启。

---

## 📄 各模块对应文件

### A — 用户系统（陈怡安）
- `A-package/WebContent/A-user/login.jsp`
- `A-package/WebContent/A-user/register.jsp`
- `A-package/WebContent/A-user/profile.jsp`
- `A-package/WebContent/A-user/editProfile.jsp`
- `A-package/src/.../servlet/LoginServlet.java`
- `A-package/src/.../servlet/RegisterServlet.java`
- `A-package/src/.../servlet/ProfileServlet.java`
- `A-package/src/.../servlet/EditProfileServlet.java`

### B — 项目发布与浏览（陈凯博）
- `B-package/WebContent/B-project/projectList.jsp`
- `B-package/WebContent/B-project/projectDetail.jsp`
- `B-package/WebContent/B-project/postProject.jsp`
- `B-package/WebContent/B-project/editProject.jsp`
- `B-package/WebContent/B-project/myProjects.jsp`
- `B-package/src/.../servlet/ProjectListServlet.java`
- `B-package/src/.../servlet/ProjectDetailServlet.java`
- `B-package/src/.../servlet/PostProjectServlet.java`
- `B-package/src/.../servlet/EditProjectServlet.java`

### C — 竞标系统（陈僖睿）
- `C-package/WebContent/C-bid/bidForm.jsp`
- `C-package/WebContent/C-bid/bidsOnProject.jsp`
- `C-package/WebContent/C-bid/myBids.jsp`
- `C-package/src/.../servlet/PlaceBidServlet.java`
- `C-package/src/.../servlet/BidListServlet.java`
- `C-package/src/.../servlet/MyBidsServlet.java`
- `C-package/src/.../servlet/AwardBidServlet.java`

### D — 订单与评价（陈子豪）
- `D-package/WebContent/D-order/orderList.jsp`
- `D-package/WebContent/D-order/orderDetail.jsp`
- `D-package/WebContent/D-order/dashboard.jsp`
- `D-package/src/.../servlet/OrderListServlet.java`
- `D-package/src/.../servlet/OrderDetailServlet.java`
- `D-package/src/.../servlet/DashboardServlet.java`
- `D-package/src/.../servlet/ReviewServlet.java`

---

## 🔧 常见问题

### 数据库连接失败？
修改对应包的 `src/com/freelite/util/DBUtil.java` 中的 `DB_USER` 和 `DB_PASSWORD`。

### 登录验证挡住了？
独立分包（A/B/C/D）的 `web.xml` 中 **AuthFilter 已注释**，直接访问即可。

