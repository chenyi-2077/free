# Freelite 🚀

> 自由职业项目竞标平台 — 轻量版 Freelancer  
> **让自由协作，如此简单**

---

## 📋 项目简介

Freelite 是一个 B/S 架构的 Web 应用，对标 Freelancer.com 的核心功能：
雇主发布项目，自由职业者竞标接单，完成后评价评分。

**技术栈**：JSP + Bootstrap 5 + Java Servlet + JDBC + MySQL 5.7 + Tomcat 8.5

---

## 👥 团队与分工

| 学号 | 姓名 | 程序模块分工 | 说明书分工 |
|---|---|---|---|
| 24030505 | **陈怡安** | 用户注册功能实现、JWT 认证鉴权模块设计与实现、会话管理、个人资料管理、用户信息编辑、权限控制、用户注销、管理员用户管理模块设计与实现 | 绪论、系统需求分析、说明书格式整合、目录、参考文献 |
| 24030503 | **陈凯博** | 雇主项目发布功能实现、项目列表分页展示、项目分类筛选、项目详情查看、项目搜索功能实现、项目状态管理、雇主项目编辑与删除功能、我的项目列表模块设计与实现 | 程序相应的模块及流程图、功能模块图、概念结构设计 |
| 24030504 | **陈僖睿** | 自由职业者竞标提交功能实现、项目竞标列表展示、雇主授标处理、我的竞标记录查询、竞标撤回功能实现、竞标消息沟通、竞标者资料查看、竞标模块设计与实现 | 程序相应的模块及流程图、致谢、功能总结 |
| 24030506 | **陈子豪** | 中标后订单创建功能实现、订单管理列表与详情、订单完成确认处理、订单取消与争议处理、评价提交功能实现、评价列表查看、数据看板统计展示、Excel 报表导出、订单与评价模块设计与实现 | 程序相应的模块及流程图、三线表 |

> **选题**：自由职业任务集市

---

## 各模块对应源码

### A — 用户系统（陈怡安）

**对应源码**：User.java、LoginServlet.java、RegisterServlet.java、ProfileServlet.java、EditProfileServlet.java、LogoutServlet.java、UserDao.java、login.jsp、register.jsp、profile.jsp、editProfile.jsp

### B — 项目发布与浏览（陈凯博）

**对应源码**：Project.java、Category.java、PostProjectServlet.java、ProjectListServlet.java、ProjectDetailServlet.java、ProjectDao.java、CategoryDao.java、postProject.jsp、projectList.jsp、projectDetail.jsp

### C — 竞标系统（陈僖睿）

**对应源码**：Bid.java、PlaceBidServlet.java、BidListServlet.java、AwardBidServlet.java、MyBidsServlet.java、BidDao.java、bidForm.jsp、bidsOnProject.jsp、myBids.jsp

### D — 订单与评价（陈子豪）

**对应源码**：Order.java、Review.java、CompleteOrderServlet.java、OrderListServlet.java、OrderDetailServlet.java、ReviewServlet.java、DashboardServlet.java、OrderDao.java、ReviewDao.java、dashboard.jsp、orderList.jsp、orderDetail.jsp

### 公用组件（陈怡安 组长整合）

DBUtil.java、index.jsp、database.sql

---

## ⚙️ 快速开始

### 环境要求

- JDK 8+
- Apache Tomcat 8.5+
- MySQL 5.7+
- IDE：Eclipse 2026-03（或任意支持 Dynamic Web Project 的 IDE）

### 运行步骤

**1. Clone 项目**

```bash
git clone https://github.com/chenyi-2077/Freelite.git
```

**2. 导入 Eclipse**

File → Import → General → Existing Projects into Workspace → 选择 Freelite 文件夹

> 确保 Eclipse 已配置 Tomcat Server：Window → Preferences → Server → Runtime Environments

**3. 创建数据库**

```bash
mysql -u root -p < docs/database.sql
```

**4. 修改数据库连接配置**

打开 `src/com/freelite/util/DBUtil.java`，按实际环境修改：

```java
private static final String URL = "jdbc:mysql://localhost:3306/freelite?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "***";
```

**5. 启动项目**

右键项目 → Run As → Run on Server → 选择 Tomcat → 浏览器自动打开 `http://localhost:8080/Freelite/`

---

## 🗂️ 项目文件结构

```
Freelite/
├── README.md
├── docs/
│   ├── database.sql
│   └── 说明书分工.md
├── WebContent/
│   ├── WEB-INF/web.xml
│   ├── A-user/         ← 用户模块（A 陈怡安）
│   ├── B-project/      ← 项目模块（B 陈凯博）
│   ├── C-bid/          ← 竞标模块（C 陈僖睿）
│   ├── D-order/        ← 订单模块（D 陈子豪）
│   └── index.jsp / index.html
├── src/com/freelite/
│   ├── model/          ← 实体类（User/Project/Category/Bid/Order/Review）
│   ├── dao/            ← 数据访问层
│   ├── servlet/        ← 控制层（15 个 Servlet）
│   └── util/           ← 工具类（DBUtil、JWTUtil）
└── .gitignore
```

---

## 📚 说明书文件

| 文件 | 负责人 | 状态 |
|---|---|---|
| `docs/说明书分工.md` | 陈怡安 | ✅ 已更新 |
| `docs/说明书_用户系统.md` | 陈怡安 | ⬜ 待编写 |
| `docs/说明书_项目发布与浏览.md` | 陈凯博 | ⬜ 待编写 |
| `docs/说明书_竞标系统.md` | 陈僖睿 | ⬜ 待编写 |
| `docs/说明书_订单与评价.md` | 陈子豪 | ⬜ 待编写 |
| `docs/说明书_整合版.md` | 陈怡安 | ⬜ 待编写 |

---

*Freelite — 让自由协作，如此简单* 🚀
