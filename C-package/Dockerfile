FROM tomcat:8.5-jdk11

# 设置时区为东八区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone

# 删除默认 ROOT 应用
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# 复制 MySQL 驱动
COPY lib/mysql-connector-j-8.0.33.jar /usr/local/tomcat/lib/

# 复制源码和 web 资源
COPY src /tmp/freelite-src/src
COPY WebContent /tmp/freelite-src/WebContent

# 编译 Java 源码（排除 *DAO.java 这类编译不了的副本）
RUN mkdir -p /tmp/classes && \
    CLASSPATH="/usr/local/tomcat/lib/servlet-api.jar:/usr/local/tomcat/lib/mysql-connector-j-8.0.33.jar" && \
    find /tmp/freelite-src/src -name "*.java" -not -name "*DAO.java" > /tmp/sources.txt && \
    javac -d /tmp/classes -cp "$CLASSPATH" @/tmp/sources.txt 2>&1 && \
    echo "=== Compile OK ===" || (echo "=== Compile had errors ===" && false)

# 构建 WAR 部署
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    cp -r /tmp/freelite-src/WebContent/* /usr/local/tomcat/webapps/ROOT/ && \
    cp -r /tmp/classes/* /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/

EXPOSE 8080
CMD ["catalina.sh", "run"]
