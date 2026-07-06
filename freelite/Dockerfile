FROM tomcat:8.5-jdk11

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY lib/mysql-connector-j-8.0.33.jar /usr/local/tomcat/lib/
COPY src /tmp/freelite-src/src
COPY WebContent /tmp/freelite-src/WebContent

RUN mkdir -p /tmp/classes && \
    CLASSPATH="/usr/local/tomcat/lib/servlet-api.jar:/usr/local/tomcat/lib/mysql-connector-j-8.0.33.jar" && \
    find /tmp/freelite-src/src -name "*.java" -not -name "*DAO.java" > /tmp/sources.txt && \
    javac -d /tmp/classes -cp "$CLASSPATH" @/tmp/sources.txt 2>&1 && \
    echo "=== Compile OK ==="

RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    cp -r /tmp/freelite-src/WebContent/* /usr/local/tomcat/webapps/ROOT/ && \
    cp -r /tmp/classes/* /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/

EXPOSE 8080
CMD ["catalina.sh", "run"]
