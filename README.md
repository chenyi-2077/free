# Freelite — 自由职业项目竞标平台

## 项目结构

本项目为课程设计独立项目版本，每个组员一个独立的 Eclipse 项目，包名为各自姓名拼音。

### 四个独立项目

| 目录 | 姓名 | 模块 | 运行URL |
|------|------|------|---------|
| `chen_yi_an/` | 陈怡安 | 用户注册/登录/资料编辑、钱包/充值、交付与沟通 | http://localhost:8080/chen_yi_an |
| `chen_kai_bo/` | 陈凯博 | 项目发布/列表/详情/编辑/删除/状态管理/搜索 | http://localhost:8080/chen_kai_bo |
| `chen_xi_rui/` | 陈僖睿 | 竞标提交/竞标列表/授标/我的竞标 | http://localhost:8080/chen_xi_rui |
| `chen_zi_hao/` | 陈子豪 | 订单列表/详情/完成确认、评价、数据看板 | http://localhost:8080/chen_zi_hao |

### 数据库

执行 `docs/database.sql` 创建数据库和测试数据。

SQL 文件包含所有表结构和测试数据（3个用户、3个项目）。

### 环境要求

- JDK 11+
- Apache Tomcat 8.5+
- MySQL 5.7+
- Eclipse 2026-03（或任意支持 Dynamic Web Project 的 IDE）

### 导入步骤

1. 每个独立项目单独导入 Eclipse：File → Import → General → Existing Projects into Workspace
2. 确保 Eclipse 已配置 Tomcat Server
3. 执行 `docs/database.sql` 创建数据库
4. 修改每个项目 `src/com/freelite/util/DBUtil.java` 中的数据库密码
5. 右键项目 → Run As → Run on Server

### 说明

- 每个项目可以独立运行，所有页面无需登录即可访问
- 演示时需要手动模拟用户身份（使用 session 中的 user 对象）
