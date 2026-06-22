package com.freelite.util;

import java.sql.*;

/**
 * 数据库连接工具类
 * 
 * 使用前请修改：URL、USERNAME、PASSWORD 为你自己的 MySQL 配置
 * 
 * 用法：
 *   Connection conn = DBUtil.getConnection();
 *   // ... 操作数据库 ...
 *   DBUtil.closeConnection(conn);
 */
public class DBUtil {

    // =======================================================
    // ★ 请修改为你的 MySQL 配置
    // =======================================================
    private static final String URL = "jdbc:mysql://localhost:3306/freelite?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
