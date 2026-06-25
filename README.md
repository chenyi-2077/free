# Freelite 🚀

> 自由职业项目竞标平台 — 轻量版 Freelancer  
> **让自由协作，如此简单**

---

## 📋 项目简介

Freelite 是一个 B/S 架构的 Web 应用，对标 Freelancer.com 的核心功能：
雇主发布项目，自由职业者竞标接单，完成后评价评分。

完整业务闭环：**发布 → 竞标 → 授标 → 担保支付 → 交付 → 资金释放 → 评价**

**技术栈**：JSP + Bootstrap 5 + Java Servlet + JDBC + MySQL 5.7 + Tomcat 8.5

---

## 👥 团队与分工

| 学号 | 姓名 | 程序模块分工 | 说明书分工 |
|---|---|---|---|
| 24030505 | **陈怡安** | 用户系统（注册/登录/资料/权限）、公用组件（DBUtil/数据库脚本）、会话与认证、**结算系统（钱包/充值/担保支付/资金释放）**、**交付与沟通模块**、**交付物文件管理**、**AuthFilter 权限过滤** | 绪论、系统需求分析、说明书格式整合、目录、参考文献 |
| 24030503 | **陈凯博** | 项目发布与编辑删除、项目列表分页/搜索/分类筛选、项目详情与状态管理（关闭/重新开放）、我的项目列表 | 程序相应的模块及流程图、功能模块图、概念结构设计 |
| 24030504 | **陈僖睿** | 竞标提交与展示、雇主授标处理、我的竞标记录、竞标消息沟通 | 程序相应的模块及流程图、致谢、功能总结 |
| 24030505 | **陈子豪** | 中标后订单创建、订单管理与详情、标记完成/确认完成、评价提交与列表、数据看板 | 程序相应的模块及流程图、三线表 |

> **选题**：自由职业任务集市

---

## 🏗️ 完整功能模块

### A — 🧑 用户系统（陈怡安）

- 用户注册（雇主/自由职业者双角色）、登录、注销
- 个人资料查看与编辑（头像、技能标签、评分展示）
- Session 会话管理 + AuthFilter 权限拦截

**对应源码**：`User.java` `LoginServlet.java` `RegisterServlet.java` `ProfileServlet.java` `EditProfileServlet.java` `LogoutServlet.java` `UserDao.java` `AuthFilter.java`  
**页面**：`A-user/login.jsp` `A-user/register.jsp` `A-user/profile.jsp` `A-user/editProfile.jsp`

### B — 📋 项目发布与浏览（陈凯博）

- 项目发布（标题/描述/预算/截止/分类）、编辑、删除
- 项目市场列表（分页、关键字搜索、分类筛选）
- 项目详情页、项目状态管理（关闭/重新开放）
- 我的项目列表

**对应源码**：`Project.java` `Category.java` `PostProjectServlet.java` `ProjectListServlet.java` `ProjectDetailServlet.java` `MyProjectsServlet.java` `EditProjectServlet.java` `DeleteProjectServlet.java` `UpdateProjectStatusServlet.java` `ProjectDao.java` `CategoryDao.java`  
**页面**：`B-project/postProject.jsp` `B-project/projectList.jsp` `B-project/projectDetail.jsp` `B-project/myProjects.jsp` `B-project/editProject.jsp`

### C — 💰 竞标系统（陈僖睿）

- 自由职业者提交竞标（报价/工期/方案）
- 项目竞标列表展示（按金额/工期排序）
- 雇主授标处理（→ 自动创建订单 + 担保冻结雇主余额）
- 我的竞标记录
- 竞标者资料查看

**对应源码**：`Bid.java` `PlaceBidServlet.java` `BidListServlet.java` `AwardBidServlet.java` `MyBidsServlet.java` `BidDao.java`  
**页面**：`C-bid/bidForm.jsp` `C-bid/bidsOnProject.jsp` `C-bid/myBids.jsp`

### D — 📦 订单与评价（陈子豪）

- 中标后订单自动创建
- 订单列表（雇主与自由职业者分视角）
- 订单详情（含状态流转、托管金额展示）
- 自由职业者标记完成、雇主确认完成
- 评价提交与评价列表
- 数据看板统计（项目/竞标/订单/评价数量）

**对应源码**：`Order.java` `Review.java` `OrderListServlet.java` `OrderDetailServlet.java` `CompleteOrderServlet.java` `ConfirmOrderServlet.java` `ReviewServlet.java` `DashboardServlet.java` `OrderDao.java` `ReviewDao.java`  
**页面**：`D-order/orderList.jsp` `D-order/orderDetail.jsp` `D-order/dashboard.jsp`

### E — 💳 结算系统（陈怡安）

- 钱包管理（余额/冻结金额展示）
- 充值功能
- **担保支付**：授标时冻结雇主余额（`fundEscrow`）
- **资金释放**：雇主确认完成后释放到自由职业者钱包（`releaseToFreelancer`）
- **退款**：项目取消时解冻退还雇主（`refundToEmployer`）
- 交易流水记录（recharge / freeze / release / refund / income）

**对应源码**：`Wallet.java` `TransactionLog.java` `EscrowService.java` `WalletServlet.java` `RechargeServlet.java` `WalletDao.java` `TransactionLogDao.java`  
**页面**：`wallet.jsp`

### F — 💬 交付与沟通（陈怡安）

- **统一交付沟通页**：项目和沟通消息 + 交付物管理双 Tab
- **消息系统**：项目参与方可发送文本消息
- **交付物上传**：自由职业者上传交付文件（UUID 命名 / 日期分目录 / 类型白名单 / 文件大小限制 / 磁盘空间检查）
- **交付物下载**：雇主下载交付文件（回退友好提示）
- **30 天自动清理**：启动时异步清理过期文件
- **权限控制**：仅项目雇主和中标自由职业者可访问

**对应源码**：`Delivery.java` `ProjectMessage.java` `DeliveryChatServlet.java` `DeliveryDownloadServlet.java` `DeliveryDao.java` `ProjectMessageDao.java`  
**页面**：`deliveryChat.jsp`

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
private static final String DB_URL = "jdbc:mysql://localhost:3306/freelite?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "***";
```

**5. 部署运行**

右键项目 → Run As → Run on Server → 选择 Tomcat → 浏览器打开 `http://localhost:8080/`

---

## 🗂️ 项目文件结构

```
Freelite/
├── README.md
├── Dockerfile
├── docs/
│   ├── database.sql
│   └── 说明书分工.md
├── WebContent/
│   ├── WEB-INF/
│   │   ├── web.xml
│   │   └── tags/navbar.jsp
│   ├── A-user/          ← 用户模块
│   ├── B-project/       ← 项目模块
│   ├── C-bid/           ← 竞标模块
│   ├── D-order/         ← 订单模块
│   ├── deliveryChat.jsp ← 交付与沟通
│   ├── wallet.jsp       ← 钱包
│   └── index.jsp
├── src/com/freelite/
│   ├── model/           ← 实体类
│   ├── dao/             ← 数据访问层
│   ├── servlet/         ← 控制层（23 个 Servlet）
│   ├── service/         ← 业务层（EscrowService）
│   ├── filter/          ← 过滤器（AuthFilter）
│   └── util/            ← 工具类（DBUtil、AuthUtil）
├── lib/                 ← JDBC 驱动
└── .gitignore
```

---

## 🔐 业务关键逻辑

### 担保支付流程

```
雇主发布项目 → 自由职业者竞标 → 雇主授标
  ├── ① 冻结雇主余额（fundEscrow）
  ├── ② 创建订单（in_progress）
  └── ③ 自由职业者可上传交付物

自由职业者标记完成 → 雇主确认完成
  ├── ① 订单状态：in_progress → awaiting_confirm → completed
  ├── ② 释放冻结资金到自由职业者钱包
  └── ③ 可选：双方互评
```

### 交付物流程

```
自由职业者上传（type白名单/UUID命名/磁盘检查）
  → 文件存储到 uploads/yyyy/MM/
  → 雇主下载（文件路径回退友好提示）
  → 30天自动清理旧文件
```

### 项目状态转换

```
open ──→ in_progress ──→ completed
  │                        ↑
  ├──→ cancelled ──────→ open (重新开放)
  └──→ (通过按钮关闭/取消)
```

---

*Freelite — 让自由协作，如此简单* 🚀
