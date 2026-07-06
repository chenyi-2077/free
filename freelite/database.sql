-- ============================================================
-- Freelite 数据库建表脚本
-- MySQL 5.7+
-- ============================================================

CREATE DATABASE IF NOT EXISTS freelite DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE freelite;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('employer', 'freelancer') NOT NULL,
    display_name VARCHAR(50),
    avatar VARCHAR(255),
    skills VARCHAR(500),
    rating DECIMAL(2,1) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 2. 分类表
-- -----------------------------------------------------------
CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO category (name) VALUES
('Web 开发'),
('移动开发'),
('UI/UX 设计'),
('文案写作'),
('数据分析'),
('其他');

-- -----------------------------------------------------------
-- 3. 项目表
-- -----------------------------------------------------------
CREATE TABLE project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    budget DECIMAL(10,2),
    deadline DATE,
    category_id INT,
    employer_id INT NOT NULL,
    status ENUM('open', 'in_progress', 'completed', 'cancelled') DEFAULT 'open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (employer_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 4. 竞标表
-- -----------------------------------------------------------
CREATE TABLE bid (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    freelancer_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    days INT NOT NULL,
    proposal TEXT,
    status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id),
    FOREIGN KEY (freelancer_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 5. 订单表
-- -----------------------------------------------------------
CREATE TABLE task_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    employer_id INT NOT NULL,
    freelancer_id INT NOT NULL,
    amount DECIMAL(10,2),
    status ENUM('in_progress', 'completed', 'cancelled') DEFAULT 'in_progress',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id),
    FOREIGN KEY (employer_id) REFERENCES user(id),
    FOREIGN KEY (freelancer_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 6. 评价表
-- -----------------------------------------------------------
CREATE TABLE review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL UNIQUE,
    from_user_id INT NOT NULL,
    to_user_id INT NOT NULL,
    score INT CHECK(score >= 1 AND score <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES task_order(id),
    FOREIGN KEY (from_user_id) REFERENCES user(id),
    FOREIGN KEY (to_user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 测试数据
-- -----------------------------------------------------------

INSERT INTO user (email, password, role, display_name, skills, rating) VALUES
('alice@test.com', '123456', 'employer', 'Alice 科技公司', NULL, 4.5),
('bob@test.com', '123456', 'freelancer', 'Bob 全栈开发', 'Java, Spring, MySQL, Vue.js', 4.8),
('carol@test.com', '123456', 'freelancer', 'Carol UI 设计师', 'Figma, Photoshop, Sketch', 4.2);

INSERT INTO project (title, description, budget, deadline, category_id, employer_id, status) VALUES
('开发一个博客系统', '需要一个支持 Markdown 的个人博客系统，包含后台管理', 5000.00, '2026-08-01', 1, 1, 'open'),
('设计 App 首页', '为我们的健身 App 设计一个全新的首页 UI', 2000.00, '2026-07-15', 3, 1, 'open'),
('数据清洗脚本', '每天自动清洗和整理 CSV 数据，输出标准格式', 1500.00, '2026-07-10', 5, 1, 'open');
