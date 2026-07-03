#!/bin/bash
# 生成四个独立 Eclipse 项目
# 每个项目使用各自包名，移除登录限制

BASE="/home/admin/.openclaw/workspace/free-main-new"

# ===================== 通用函数 =====================

make_dirs() {
    local pkg="$1"
    local pkg_dir="$2"
    mkdir -p "$pkg_dir/src/$pkg"
    mkdir -p "$pkg_dir/src/com/freelite/util"
    mkdir -p "$pkg_dir/WebContent/WEB-INF/lib"
    mkdir -p "$pkg_dir/WebContent/$pkg"
    mkdir -p "$pkg_dir/.settings"
    mkdir -p "$pkg_dir/build/classes"
}

write_eclipse_config() {
    local pkg="$1"  # e.g. chen_yi_an
    local dir="$2"

    # .classpath
    cat > "$dir/.classpath" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="src" path="src"/>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-11"/>
    <classpathentry kind="con" path="org.eclipse.jst.server.core.container/org.eclipse.jst.server.tomcat.runtimeTarget/Apache Tomcat v8.5"/>
    <classpathentry kind="output" path="build/classes"/>
</classpath>
EOF

    # .project
    cat > "$dir/.project" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
    <name>${pkg}</name>
    <comment></comment>
    <projects></projects>
    <buildSpec>
        <buildCommand>
            <name>org.eclipse.jdt.core.javabuilder</name>
            <arguments></arguments>
        </buildCommand>
        <buildCommand>
            <name>org.eclipse.wst.common.project.facet.core.builder</name>
            <arguments></arguments>
        </buildCommand>
        <buildCommand>
            <name>org.eclipse.wst.validation.validationbuilder</name>
            <arguments></arguments>
        </buildCommand>
    </buildSpec>
    <natures>
        <nature>org.eclipse.jem.workbench.JavaEMFNature</nature>
        <nature>org.eclipse.wst.common.modulecore.ModuleCoreNature</nature>
        <nature>org.eclipse.wst.common.project.facet.core.nature</nature>
        <nature>org.eclipse.jdt.core.javanature</nature>
        <nature>org.eclipse.wst.jsdt.core.jsNature</nature>
    </natures>
</projectDescription>
EOF

    # .settings/*
    cat > "$dir/.settings/org.eclipse.jdt.core.prefs" << EOF
eclipse.preferences.version=1
org.eclipse.jdt.core.compiler.codegen.targetPlatform=11
org.eclipse.jdt.core.compiler.compliance=11
org.eclipse.jdt.core.compiler.source=11
EOF

    cat > "$dir/.settings/org.eclipse.wst.common.component" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project-modules id="moduleCoreId" project-version="1.5.0">
    <wb-module deploy-name="${pkg}">
        <wb-resource deploy-path="/" source-path="/WebContent"/>
        <wb-resource deploy-path="/WEB-INF/classes" source-path="/src"/>
        <property name="context-root" value="${pkg}"/>
        <property name="java-output-path" value="/${pkg}/build/classes"/>
    </wb-module>
</project-modules>
EOF

    cat > "$dir/.settings/org.eclipse.wst.common.project.facet.core.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<faceted-project>
  <fixed facet="wst.jsdt.web"/>
  <installed facet="java" version="11"/>
  <installed facet="jst.web" version="3.1"/>
  <installed facet="wst.jsdt.web" version="1.0"/>
</faceted-project>
EOF
}

write_webxml_simple() {
    local pkg="$1"
    local dir="$2"
    cat > "$dir/WebContent/WEB-INF/web.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
    <display-name>${pkg}</display-name>
    <welcome-file-list><welcome-file>index.jsp</welcome-file></welcome-file-list>
</web-app>
EOF
}

write_webxml_full() {
    local pkg="$1"
    local dir="$2"
    # 由具体项目覆盖
    true
}

write_dbutil() {
    local dir="$1"
    cat > "$dir/src/com/freelite/util/DBUtil.java" << 'EOF'
package com.freelite.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具 — 改 DB_PASSWORD 为你的 MySQL 密码
 */
public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/freelite?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Aa20185476";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        try (java.sql.Statement st = conn.createStatement()) {
            st.execute("SET NAMES utf8mb4");
        }
        return conn;
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable r : resources) {
            if (r != null) {
                try { r.close(); } catch (Exception ignored) {}
            }
        }
    }
}
EOF
}

write_index_jsp() {
    local pkg="$1"
    local dir="$2"
    cat > "$dir/WebContent/index.jsp" << JSP
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String redirect = request.getContextPath() + "/" + (session.getAttribute("user") != null ? "projects" : "login");
    response.sendRedirect(redirect);
%>
JSP
}
